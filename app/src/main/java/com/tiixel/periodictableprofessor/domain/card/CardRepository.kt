package com.tiixel.periodictableprofessor.domain.card

import com.tiixel.periodictableprofessor.domain.ReviewData
import io.reactivex.Completable
import io.reactivex.Single

interface CardRepository {

    fun getReviewLog(): Single<List<ReviewData>>

    fun logReview(review: ReviewData): Completable
}