package com.tiixel.periodictableprofessor.datasource.card

import com.tiixel.periodictableprofessor.datasource.card.generic.StoredMnemonic
import com.tiixel.periodictableprofessor.datasource.card.generic.StoredReviewData
import com.tiixel.periodictableprofessor.datasource.card.generic.StoredUserNote
import com.tiixel.periodictableprofessor.datasource.element.ElementLocalDataSource
import com.tiixel.periodictableprofessor.domain.Card
import com.tiixel.periodictableprofessor.domain.ReviewData
import com.tiixel.periodictableprofessor.domain.card.CardRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val mnemonicDataSource: MnemonicLocalDataSource,
    private val reviewDataSource: ReviewLocalDataSource,
    private val userNoteDataSource: UserNoteLocalDataSource,
    private val elementDataSource: ElementLocalDataSource
) : CardRepository {

    override fun getReviewLog(): Single<List<ReviewData>> {
        return reviewDataSource.getReviewLog()
            .map { it.map { StoredReviewData.toDomain(it) } }
    }

    override fun updateCard(card: Card): Completable {
        TODO()
    }

    override fun logReview(review: ReviewData): Completable {
        return reviewDataSource.logReviewAndUpdateQueue(StoredReviewData.fromDomain(review))
    }

    override fun getCard(element: Byte): Single<Card> {
        return elementDataSource.getElement(element.toInt())
            .zipWith(mnemonicDataSource.getMnemonic(element))
            .zipWith(userNoteDataSource.getUserNote(element))
            .map {
                Card(
                    element = it.first.first,
                    mnemonic = StoredMnemonic.toDomain(it.first.second),
                    userNote = StoredUserNote.toDomain(it.second)
                )
            }
    }
}