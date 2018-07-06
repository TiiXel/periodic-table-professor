package com.tiixel.periodictableprofessor.domain.card.contract

import com.tiixel.periodictableprofessor.domain.Review
import io.reactivex.Completable
import io.reactivex.Single

interface CardRepository {

    fun getReviewableIds(): Single<List<Byte>>

    fun getReviewHistory(): Single<List<Review>>

    fun logReview(review: Review): Completable
}