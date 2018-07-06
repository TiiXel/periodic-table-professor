package com.tiixel.periodictableprofessor.datarepository.card

import com.tiixel.periodictableprofessor.datarepository.card.contract.ReviewLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.card.mapper.ReviewMapper
import com.tiixel.periodictableprofessor.domain.Review
import com.tiixel.periodictableprofessor.domain.card.contract.CardRepository
import com.tiixel.periodictableprofessor.domain.element.contract.ElementRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewLocalDataSource,
    private val elementRepository: ElementRepository
) : CardRepository {

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