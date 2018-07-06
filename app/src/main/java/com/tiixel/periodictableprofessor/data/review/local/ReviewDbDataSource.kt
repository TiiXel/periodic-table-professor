package com.tiixel.periodictableprofessor.data.review.local

import com.tiixel.periodictableprofessor.data.LocalDatabase
import com.tiixel.periodictableprofessor.data.review.local.db.entity.ReviewDataEntity
import com.tiixel.periodictableprofessor.datarepository.card.ReviewLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.card.generic.GenericReviewData
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ReviewDbDataSource @Inject constructor(
    private val database: LocalDatabase
) : ReviewLocalDataSource {

    override fun logReviewAndUpdateQueue(genericReviewData: GenericReviewData): Completable {
        return Completable.defer {
            database.reviewDao().logReview(ReviewDataEntity.fromGeneric(genericReviewData))
            Completable.complete()
        }
    }

    override fun getReviewLog(): Single<List<GenericReviewData>> {
        return Single.defer {
            val r = database.reviewDao().getReviewLog().map { ReviewDataEntity.toGeneric(it) }
            Single.just(r)
        }
    }
}