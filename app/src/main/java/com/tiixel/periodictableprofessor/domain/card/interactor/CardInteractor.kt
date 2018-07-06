package com.tiixel.periodictableprofessor.domain.card.interactor

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.ReviewPerformance
import com.tiixel.periodictableprofessor.domain.ReviewableFace
import com.tiixel.periodictableprofessor.domain.exception.NoCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsDueSoonException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import io.reactivex.Completable
import io.reactivex.Single
import java.util.Date

interface CardInteractor {

    /**
     * May pass the following exceptions downstream :
     * - [NoCardsAreNewException]
     */
    fun getNewCardForReview(): Single<Pair<Element, ReviewableFace>>

    /**
     * May pass the following exceptions downstream :
     * - [NoNextReviewException]
     * - [NoCardsDueSoonException]
     */
    fun getNextCardForReview(dueSoonOnly: Boolean = true): Single<Pair<Element, ReviewableFace>>

    fun getNextReviewDate(): Single<Date>

    fun countCardsDueToday(reference: Date): Single<Int>

    fun countCardsDueSoon(reference: Date): Single<Int>

    fun countCardsNewOnDay(day: Date): Single<Int>

    fun reviewCard(element: Byte, performance: ReviewPerformance, time: Date = Date()): Completable
}