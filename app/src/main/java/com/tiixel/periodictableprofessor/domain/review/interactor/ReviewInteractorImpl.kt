package com.tiixel.periodictableprofessor.domain.review.interactor

import com.tiixel.periodictableprofessor.domain.algorithm.Sm2Plus
import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.domain.exception.NoNewReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewSoonException
import com.tiixel.periodictableprofessor.domain.review.Review
import com.tiixel.periodictableprofessor.domain.review.Reviewable
import com.tiixel.periodictableprofessor.domain.review.ReviewableFace
import com.tiixel.periodictableprofessor.domain.review.contract.ReviewRepository
import com.tiixel.periodictableprofessor.domain.review.contract.ReviewableProvider
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import org.apache.commons.lang3.time.DateUtils
import java.util.Calendar
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
            .map { it.values.sortedBy { it.nextOverdue(Date()) }.last().nextDueDate }
    }

    override fun countReviewsDuePerPeriod(granularity: Int): Single<Map<Date, Int>> {
        return reviewRepository.getReviewHistory()
            // Group by due date truncated on period
            .map { it.groupBy { DateUtils.truncate(it.nextDueDate, granularity) } }
            // Group by itemId
            .map { it.mapValues { it.value.groupBy { it.item.itemId } } }
            // Sort values in itemId groups by next due date
            .map { it.mapValues { it.value.mapValues { it.value.sortedBy { it.nextDueDate } } } }
            // Keep items which are `due on this period` or `overdue and last of their group (ie we missed them this period)`
            .map {
                it.mapValues { dateMapEntry ->
                    dateMapEntry.value.mapValues { reviewsForItem ->
                        reviewsForItem.value.filter {
                            it.nextIsDueOnPeriod(dateMapEntry.key, Calendar.DATE)
                                || (it.nextIsOverdue(dateMapEntry.key) && it == reviewsForItem.value.last())
                        }
                    }.size
                }
            }
            .map { it.fillHolesInDateMap(granularity) }
            .map { it.toSortedMap() }
    }

    override fun countReviewsDueSoon(relativeTo: Date): Single<Int> {
        return reviewRepository.getReviewHistory()
            // Remove reviews not due soon
            .map { it.filter { it.nextIsDueSoon(relativeTo) } }
            // Group by itemId, keep values
            .map { it.groupBy { it.item.itemId }.values }
            // Return total size, as each item can only have one review due soon at a given time
            .map { it.sumBy { it.size } }
    }

    override fun countReviewsPerPeriod(granularity: Int): Single<Map<Date, Int>> {
        return reviewRepository.getReviewHistory()
            .map { it.groupBy { DateUtils.truncate(it.reviewDate, granularity) } }
            .map { it.mapValues { it.value.size } }
            .map { it.fillHolesInDateMap(granularity) }
            .map { it.toSortedMap() }
    }

    override fun countReviewablesNewPerPeriod(granularity: Int): Single<Map<Date, Int>> {
        return reviewRepository.getReviewHistory()
            .map { it.groupBy { it.item.itemId } }
            .map { it.map { it.value.sortedBy { it.reviewDate }.first() } }
            .map { it.groupBy { DateUtils.truncate(it.reviewDate, granularity) } }
            .map { it.mapValues { it.value.size } }
            .map { it.fillHolesInDateMap(granularity) }
            .map { it.toSortedMap() }
    }

    override fun countKnownReviewablesPerPeriod(granularity: Int): Single<Map<Date, Int>> {
        return reviewRepository.getReviewHistory()
            // Group reviews by itemId
            .map { it.groupBy { it.item.itemId } }
            // Zip with review dates truncated on granularity
            .zipWith(
                reviewRepository.getReviewHistory()
                    .map { it.groupBy { DateUtils.truncate(it.reviewDate, granularity) }.keys }
            )
            // Pair each date with the last review of each review
            .map { ReviewsPerId_Dates ->
                // Iterate over all dates
                ReviewsPerId_Dates.second.map { date ->
                    // Map to date the result of the iteration over all reviews
                    date to ReviewsPerId_Dates.first.map { reviewsForId ->
                        // Remove reviews in the future relative to the date, sort by date, keep last
                        reviewsForId.value.filter { DateUtils.truncate(it.reviewDate, granularity) <= date }
                            .sortedBy { it.reviewDate }.lastOrNull()
                    }
                }.toMap()
            }
            .map { it.mapValues { it.value.filter { it != null && it.isKnown() } } }
            .map { it.mapValues { it.value.size } }
            .map { it.fillHolesInDateMap(granularity) }
            .map { it.toSortedMap() }
    }

    override fun review(freshReview: Review.FreshReview): Completable {
        return getLastReviewForEach()
            .flatMapMaybe { if (it.containsKey(freshReview.item.itemId)) Maybe.just(it[freshReview.item.itemId]) else Maybe.empty() }
            .map { it.aggregateNewReview(freshReview, Sm2Plus::aggregator) }
            .defaultIfEmpty(Sm2Plus.defaultReview(freshReview))
            .flatMapCompletable { reviewRepository.logReview(it) }
    }

    private fun <T : Number> Map<Date, T>.fillHolesInDateMap(granularity: Int): Map<Date, T> {
        if (isEmpty()) return this

        val start = keys.sorted().first()
        val end = DateUtils.truncate(maxOf(Date(), keys.sorted().last()), granularity)
        val c = Calendar.getInstance().apply { time = start }
        val dates = mutableListOf<Date>()
        while (c.time <= end) {
            dates.add(c.time)
            c.add(granularity, 1)
        }
        val r = toMutableMap<Date, Number>()
        dates.forEach { date ->
            if (!containsKey(date)) r[date] = 0
        }
        return r as Map<Date, T>
    }
}