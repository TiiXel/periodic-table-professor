package com.tiixel.periodictableprofessor.datasource.card

import com.tiixel.periodictableprofessor.datasource.card.generic.StoredReviewData
import io.reactivex.Completable
import io.reactivex.Single

interface ReviewLocalDataSource {

    fun logReviewAndUpdateQueue(storedReviewData: StoredReviewData): Completable

    fun getReviewLog(): Single<List<StoredReviewData>>
}