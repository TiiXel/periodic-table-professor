package com.tiixel.periodictableprofessor.domain.review.interactor

import com.tiixel.periodictableprofessor.domain.Review
import io.reactivex.Completable
import io.reactivex.Single
import java.util.Date

interface ReviewInteractor {

    fun getReviewForNew(): Single<Review.UpcomingReview>

    fun getReviewForNext(dueSoonOnly: Boolean = true): Single<Review.UpcomingReview>

    fun getNextReviewDate(): Single<Date>

    fun countReviewsDueOnDay(relativeTo: Date): Single<Int>

    fun countReviewsDueSoon(relativeTo: Date): Single<Int>

    fun countReviewsOnDay(relativeTo: Date): Single<Int>

    fun countReviewablesNewOnDay(relativeTo: Date): Single<Int>

    fun review(freshReview: Review.FreshReview): Completable
}