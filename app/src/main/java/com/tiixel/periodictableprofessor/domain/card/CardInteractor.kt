package com.tiixel.periodictableprofessor.domain.card

import com.tiixel.periodictableprofessor.domain.Card
import com.tiixel.periodictableprofessor.domain.exception.AllCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsDueSoonException
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.Date

interface CardInteractor {

    /**
     * May pass the following exceptions downstream :
     * - [NoCardsAreNewException]
     */
    fun getNewCardForReview(): Single<Pair<Card, Card.Companion.Face>>

    /**
     * May pass the following exceptions downstream :
     * - [AllCardsAreNewException]
     * - [NoCardsDueSoonException]
     */
    fun getNextCardForReview(dueSoonOnly: Boolean = true): Single<Pair<Card, Card.Companion.Face>>

    fun getNextReviewDate(): Maybe<Date>

    fun countCardsDueToday(reference: Date): Single<Int>

    fun countCardsDueSoon(reference: Date): Single<Int>

    fun countCardsNewOnDay(day: Date): Single<Int>

    fun reviewCard(element: Byte, performance: Card.Companion.Performance, time: Date = Date()): Completable

    fun editCard(card: Card): Completable
}