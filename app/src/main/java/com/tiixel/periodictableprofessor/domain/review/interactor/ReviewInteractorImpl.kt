package com.tiixel.periodictableprofessor.domain.review.interactor

import com.tiixel.periodictableprofessor.domain.review.Review
import com.tiixel.periodictableprofessor.domain.review.Reviewable
import com.tiixel.periodictableprofessor.domain.review.ReviewableFace
import com.tiixel.periodictableprofessor.domain.algorithm.Sm2Plus
import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.domain.exception.NoNewReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewSoonException
import com.tiixel.periodictableprofessor.domain.review.contract.ReviewRepository
import com.tiixel.periodictableprofessor.domain.review.contract.ReviewableProvider
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import org.apache.commons.lang3.time.DateUtils
import java.util.Date
import java.util.Random
import javax.inject.Inject

class ReviewInteractorImpl @Inject constructor(
    private val reviewRepository: ReviewRepository,
    elementInteractor: ElementInteractor
) : ReviewInteractor {

    private val reviewableProviders: List<ReviewableProvider> = listOf(elementInteractor as ReviewableProvider)


    private fun getReviewableIds(): Single<List<Byte>> {
        return Single.merge(reviewableProviders.map { it.getReviewableIds() }).reduce(listOf()) { l1, l2 -> l1 + l2 }
    }

    private fun getLastReviewForEach(): Single<Map<Byte, Review>> {
        return reviewRepository.getReviewHistory()
            .map { it.groupBy { it.item.itemId } }
            .map { it.map { it.key to it.value.sortedBy { it.reviewDate }.last() }.toMap() }
    }

    override fun getReviewForNew(): Single<Review.UpcomingReview> {
        return getReviewableIds()
            .zipWith(getLastReviewForEach()) { ids, next -> ids.filter { it !in next.keys } }
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoNewReviewException))
            .map { it.shuffled().first() }
            .map { Review.UpcomingReview(Reviewable(it), ReviewableFace.PICTURE) }
    }

    override fun getReviewForNext(dueSoonOnly: Boolean): Single<Review.UpcomingReview> {
        return getLastReviewForEach()
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoNextReviewException))
            .map { if (dueSoonOnly) it.filter { it.value.nextIsDueSoon(Date()) } else it }
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoNextReviewSoonException))
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
        return getLastReviewForEach()
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoNextReviewException))
            .map { it.toList().sortedBy { it.second.nextOverdue(Date()) }.last().second.nextDueDate }
    }

    override fun countReviewsDueOnDay(relativeTo: Date): Single<Int> {
        return getLastReviewForEach()
            .map { it.filter { it.value.nextIsDueOnDay(relativeTo) || it.value.nextIsOverdue(relativeTo) }.size }
    }

    override fun countReviewsDueSoon(relativeTo: Date): Single<Int> {
        return getLastReviewForEach()
            .map { it.filter { it.value.nextIsDueSoon(relativeTo) || it.value.nextIsOverdue(relativeTo) }.size }
    }

    override fun countReviewsOnDay(relativeTo: Date): Single<Int> {
        return reviewRepository.getReviewHistory()
            .map { it.filter { DateUtils.isSameDay(it.reviewDate, relativeTo) }.size }
    }

    override fun countReviewablesNewOnDay(relativeTo: Date): Single<Int> {
        return reviewRepository.getReviewHistory()
            .map { it.groupBy { it.item.itemId } }
            .map { it.map { it.key to it.value.sortedBy { it.reviewDate }.first() } }
            .map { it.filter { DateUtils.isSameDay(it.second.reviewDate, relativeTo) }.size }
    }

    override fun review(freshReview: Review.FreshReview): Completable {
        return getLastReviewForEach()
            .flatMapMaybe { if (it.containsKey(freshReview.item.itemId)) Maybe.just(it[freshReview.item.itemId]) else Maybe.empty() }
            .map { it.aggregateNewReview(freshReview, Sm2Plus::aggregator) }
            .defaultIfEmpty(Sm2Plus.defaultReview(freshReview))
            .flatMapCompletable { reviewRepository.logReview(it) }
    }
}