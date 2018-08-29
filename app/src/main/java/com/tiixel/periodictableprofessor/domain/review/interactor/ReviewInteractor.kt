package com.tiixel.periodictableprofessor.domain.review.interactor

import com.tiixel.periodictableprofessor.domain.exception.NoNewReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewSoonException
import com.tiixel.periodictableprofessor.domain.review.Review
import com.tiixel.periodictableprofessor.domain.review.Review.FreshReview
import com.tiixel.periodictableprofessor.domain.review.Review.UpcomingReview
import com.tiixel.periodictableprofessor.domain.review.Reviewable
import com.tiixel.periodictableprofessor.domain.review.ReviewableFace
import io.reactivex.Completable
import io.reactivex.Single
import java.util.Date

interface ReviewInteractor {

    /**
     * Returns an [UpcomingReview] for a [Reviewable] that was never
     * reviewed.
     *
     * The [UpcomingReview.face] is always set to [ReviewableFace.PICTURE].
     *
     * May return errors:
     *   - [NoNewReviewException]
     */
    fun getReviewForNew(): Single<UpcomingReview>

    /**
     * Returns an [UpcomingReview] for a [Reviewable] that has already
     * been reviewed.
     *
     * The [UpcomingReview.face] is set to:
     *   - [ReviewableFace.PICTURE] if the last review for this reviewable has
     *   [Review.isKnown] to false.
     *   - A random face from [ReviewableFace] else.
     *
     * If [dueSoonOnly] is true, the UpcomingReview's reviewable is to be
     * reviewed soon according to [Review.nextIsDueSoon].
     *
     * May return errors:
     *   - [NoNextReviewException]
     *   - [NoNextReviewSoonException]
     */
    fun getReviewForNext(dueSoonOnly: Boolean = true): Single<UpcomingReview>

    /**
     * Returns the date when the next review is scheduled.
     */
    fun getNextReviewDate(): Single<Date>

    /**
     * Returns the number of reviews due at the end of each period.
     */
    fun countReviewsDuePerPeriod(granularity: Int): Single<Map<Date, Int>>

    /**
     * Returns the number of reviews that are scheduled soon according to
     * [Review.nextIsDueSoon].
     */
    fun countReviewsDueSoon(relativeTo: Date): Single<Int>

    /**
     * Returns the number of reviews done in each period.
     */
    fun countReviewsPerPeriod(granularity: Int): Single<Map<Date, Int>>

    /**
     * Returns the number of [Reviewable]s that were reviewed for the first time
     * in each period.
     */
    fun countReviewablesNewPerPeriod(granularity: Int): Single<Map<Date, Int>>

    /**
     * Returns the number of [Reviewable]s known at the end of each period.
     */
    fun countKnownReviewablesPerPeriod(granularity: Int): Single<Map<Date, Int>>

    /**
     * Review a [Reviewable]: log a new [Review] for the [FreshReview] given.
     */
    fun review(freshReview: FreshReview): Completable
}