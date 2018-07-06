package com.tiixel.periodictableprofessor.domain.card.interactor

import com.tiixel.periodictableprofessor.domain.Review
import io.reactivex.Completable
import io.reactivex.Single
import java.util.Date

interface CardInteractor {

    fun getNewCardForReview(): Single<Review.UpcomingReview>

    fun getNextCardForReview(dueSoonOnly: Boolean = true): Single<Review.UpcomingReview>

    fun getNextReviewDate(): Single<Date>

    fun countCardsDueToday(reference: Date): Single<Int>

    fun countCardsDueSoon(reference: Date): Single<Int>

    fun countCardsNewOnDay(day: Date): Single<Int>

    fun reviewCard(freshReview: Review.FreshReview): Completable
}