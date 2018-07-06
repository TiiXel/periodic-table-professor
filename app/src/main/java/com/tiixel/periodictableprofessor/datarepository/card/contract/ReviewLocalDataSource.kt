package com.tiixel.periodictableprofessor.datarepository.card.contract

import com.tiixel.periodictableprofessor.datarepository.card.generic.GenericReview
import io.reactivex.Completable
import io.reactivex.Single

interface ReviewLocalDataSource {

    fun logReview(genericReview: GenericReview): Completable

    fun getReviewHistory(): Single<List<GenericReview>>
}