package com.tiixel.periodictableprofessor.domain.review

import org.apache.commons.lang3.time.DateUtils
import java.util.Date

data class Review(
    val item: Reviewable,
    val face: ReviewableFace,
    val reviewDate: Date,
    val performance: ReviewPerformance,
    val nextDueDate: Date,

    val aggregatedItemDifficulty: Float
) {

    fun nextInterval(): Long = nextDueDate.time - reviewDate.time

    fun nextOverdue(relativeTo: Date): Float = (relativeTo.time - reviewDate.time).toFloat() / nextInterval().toFloat()

    fun nextIsOverdue(relativeTo: Date): Boolean = nextOverdue(relativeTo) > 1

    fun nextIsDueSoon(relativeTo: Date): Boolean = nextOverdue(relativeTo) >= 0.75

    fun nextIsDueOnPeriod(relativeTo: Date, granularity: Int): Boolean =
        DateUtils.truncate(nextDueDate, granularity) == DateUtils.truncate(relativeTo, granularity)

    fun aggregateNewReview(freshReview: FreshReview, aggregator: (Review, FreshReview) -> Review): Review {
        return aggregator(this, freshReview)
    }

    fun isKnown(): Boolean = nextInterval() > 40000000

    data class UpcomingReview(
        val item: Reviewable,
        val face: ReviewableFace
    )

    data class FreshReview(
        val item: Reviewable,
        val face: ReviewableFace,
        val reviewDate: Date = Date(),
        val performance: ReviewPerformance
    )
}