package com.tiixel.periodictableprofessor.domain.card.interactor

import com.tiixel.periodictableprofessor.domain.Review
import com.tiixel.periodictableprofessor.domain.Reviewable
import com.tiixel.periodictableprofessor.domain.ReviewableFace
import com.tiixel.periodictableprofessor.domain.algorithm.Sm2Plus
import com.tiixel.periodictableprofessor.domain.card.contract.CardRepository
import com.tiixel.periodictableprofessor.domain.exception.NoCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsDueSoonException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import org.apache.commons.lang3.time.DateUtils
import java.util.Date
import java.util.Random
import javax.inject.Inject

class CardInteractorImpl @Inject constructor(
    private val cardRepository: CardRepository
) : CardInteractor {

    private fun getLastReviews(): Single<Map<Byte, Review>> {
        return cardRepository.getReviewHistory()
            .map { it.groupBy { it.item.itemId } }
            .map { it.map { it.key to it.value.sortedBy { it.reviewDate }.last() }.toMap() }
    }

    override fun getNewCardForReview(): Single<Review.UpcomingReview> {
        return cardRepository.getReviewableIds()
            .zipWith(getLastReviews()) { ids, next -> ids.filter { it !in next.keys } }
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoCardsAreNewException))
            .map { it.shuffled().first() }
            .map { Review.UpcomingReview(Reviewable(it), ReviewableFace.PICTURE) }
    }

    override fun getNextCardForReview(dueSoonOnly: Boolean): Single<Review.UpcomingReview> {
        return getLastReviews()
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoNextReviewException))
            .map { if (dueSoonOnly) it.filter { it.value.nextIsDueSoon(Date()) } else it }
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoCardsDueSoonException))
            .map { it.toList().sortedBy { it.second.nextOverdue(Date()) }.last().second }
            .map { it.item.itemId to it.isKnown() }
            .map {
                it.first to
                    if (it.second)
                        ReviewableFace.values()[Random().nextInt(ReviewableFace.values().size)]
                    else
                        ReviewableFace.PICTURE
            }
            .map { Review.UpcomingReview(Reviewable(it.first), ReviewableFace.SYMBOL) }
    }

    override fun getNextReviewDate(): Single<Date> {
        return getLastReviews()
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoNextReviewException))
            .map { it.toList().sortedBy { it.second.nextOverdue(Date()) }.last().second.nextDueDate }
    }

    override fun countCardsDueToday(reference: Date): Single<Int> {
        return getLastReviews()
            .map { it.filter { it.value.nextIsDueOnDay(reference) || it.value.nextIsOverdue(reference) }.size }
    }

    override fun countCardsDueSoon(reference: Date): Single<Int> {
        return getLastReviews()
            .map { it.filter { it.value.nextIsDueSoon(reference) || it.value.nextIsOverdue(reference) }.size }
    }

    override fun countCardsNewOnDay(day: Date): Single<Int> {
        return cardRepository.getReviewHistory()
            .map { it.groupBy { it.item.itemId } }
            .map { it.map { it.key to it.value.sortedBy { it.reviewDate }.first() } }
            .map { it.filter { DateUtils.isSameDay(it.second.reviewDate, day) }.size }
    }

    override fun reviewCard(freshReview: Review.FreshReview): Completable {
        return getLastReviews()
            .flatMapMaybe { if (it.containsKey(freshReview.item.itemId)) Maybe.just(it[freshReview.item.itemId]) else Maybe.empty() }
            .map { it.aggregateNewReview(freshReview, Sm2Plus::aggregator) }
            .defaultIfEmpty(Sm2Plus.defaultReview(freshReview))
            .flatMapCompletable { cardRepository.logReview(it) }
    }
}