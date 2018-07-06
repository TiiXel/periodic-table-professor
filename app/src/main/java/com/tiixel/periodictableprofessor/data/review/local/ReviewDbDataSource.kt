package com.tiixel.periodictableprofessor.data.review.local

import com.tiixel.periodictableprofessor.data.LocalDatabase
import com.tiixel.periodictableprofessor.data.review.local.db.entity.ReviewEntity
import com.tiixel.periodictableprofessor.datarepository.review.contract.ReviewLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.review.generic.GenericReview
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ReviewDbDataSource @Inject constructor(
    private val database: LocalDatabase
) : ReviewLocalDataSource {

    override fun logReview(genericReview: GenericReview): Completable {
        return Completable.defer {
            database.reviewDao().logReview(ReviewEntity.fromGeneric(genericReview))
            Completable.complete()
        }
    }

    override fun getReviewHistory(): Single<List<GenericReview>> {
        return Single.defer {
            val r = database.reviewDao().getReviewLog().map { ReviewEntity.toGeneric(it) }
            Single.just(r)
        }
    }
}