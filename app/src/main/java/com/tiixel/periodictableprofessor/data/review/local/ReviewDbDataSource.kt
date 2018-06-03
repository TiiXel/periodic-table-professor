package com.tiixel.periodictableprofessor.data.review.local

import com.tiixel.periodictableprofessor.data.LocalDatabase
import com.tiixel.periodictableprofessor.data.review.local.db.entity.ReviewDataEntity
import com.tiixel.periodictableprofessor.datasource.card.ReviewLocalDataSource
import com.tiixel.periodictableprofessor.datasource.card.generic.StoredReviewData
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ReviewDbDataSource @Inject constructor(
    private val database: LocalDatabase
) : ReviewLocalDataSource {

    override fun logReviewAndUpdateQueue(storedReviewData: StoredReviewData): Completable {
        return Completable.defer {
            database.reviewDao().logReview(ReviewDataEntity.fromGeneric(storedReviewData))
            Completable.complete()
        }
    }

    override fun getReviewLog(): Single<List<StoredReviewData>> {
        return Single.defer {
            val r = database.reviewDao().getReviewLog().map { ReviewDataEntity.toGeneric(it) }
            Single.just(r)
        }
    }
}