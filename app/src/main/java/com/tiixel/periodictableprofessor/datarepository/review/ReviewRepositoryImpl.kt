package com.tiixel.periodictableprofessor.datarepository.review

import com.tiixel.periodictableprofessor.datarepository.review.contract.ReviewLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.review.mapper.ReviewMapper
import com.tiixel.periodictableprofessor.domain.Review
import com.tiixel.periodictableprofessor.domain.element.contract.ElementRepository
import com.tiixel.periodictableprofessor.domain.review.contract.ReviewRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewLocalDataSource,
    private val elementRepository: ElementRepository
) : ReviewRepository {

    override fun getReviewableIds(): Single<List<Byte>> {
        return elementRepository.getElements()
            .map { it.filter { it.mnemonicPhrase != null && it.mnemonicPicture != null } }
            .map { it.map { it.atomicNumber } }
    }

    override fun getReviewHistory(): Single<List<Review>> {
        return reviewDataSource.getReviewHistory()
            .map { it.map { ReviewMapper.toDomain(it) } }
    }

    override fun logReview(review: Review): Completable {
        return reviewDataSource.logReview(ReviewMapper.toGeneric(review))
    }
}