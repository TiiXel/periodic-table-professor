package com.tiixel.periodictableprofessor.datarepository.card.contract

import com.tiixel.periodictableprofessor.datarepository.card.generic.GenericReviewData
import io.reactivex.Completable
import io.reactivex.Single

interface ReviewLocalDataSource {

    fun logReviewAndUpdateQueue(genericReviewData: GenericReviewData): Completable

    fun getReviewLog(): Single<List<GenericReviewData>>
}