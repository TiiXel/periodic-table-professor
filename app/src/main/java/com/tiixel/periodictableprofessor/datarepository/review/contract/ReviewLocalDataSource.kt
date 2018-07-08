package com.tiixel.periodictableprofessor.datarepository.review.contract

import com.tiixel.periodictableprofessor.datarepository.review.generic.GenericReview
import io.reactivex.Completable
import io.reactivex.Single

interface ReviewLocalDataSource {

    fun logReview(genericReview: GenericReview): Completable

    fun getReviewHistory(): Single<List<GenericReview>>
}