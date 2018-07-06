package com.tiixel.periodictableprofessor.datarepository.card

import com.tiixel.periodictableprofessor.datarepository.card.generic.StoredReviewData
import com.tiixel.periodictableprofessor.domain.ReviewData
import com.tiixel.periodictableprofessor.domain.card.CardRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewLocalDataSource
) : CardRepository {

    override fun getReviewLog(): Single<List<ReviewData>> {
        return reviewDataSource.getReviewLog()
            .map { it.map { StoredReviewData.toDomain(it) } }
    }

    override fun logReview(review: ReviewData): Completable {
        return reviewDataSource.logReviewAndUpdateQueue(StoredReviewData.fromDomain(review))
    }
}