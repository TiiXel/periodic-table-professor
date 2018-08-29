package com.tiixel.periodictableprofessor.domain.review.contract

import com.tiixel.periodictableprofessor.domain.review.Review
import io.reactivex.Completable
import io.reactivex.Single

interface ReviewRepository {

    fun getReviewHistory(): Single<List<Review>>

    fun logReview(review: Review): Completable
}