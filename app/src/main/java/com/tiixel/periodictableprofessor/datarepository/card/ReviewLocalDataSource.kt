package com.tiixel.periodictableprofessor.datarepository.card

import com.tiixel.periodictableprofessor.datarepository.card.generic.StoredReviewData
import io.reactivex.Completable
import io.reactivex.Single

interface ReviewLocalDataSource {

    fun logReviewAndUpdateQueue(storedReviewData: StoredReviewData): Completable

    fun getReviewLog(): Single<List<StoredReviewData>>
}