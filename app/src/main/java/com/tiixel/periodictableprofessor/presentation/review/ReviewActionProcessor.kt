package com.tiixel.periodictableprofessor.presentation.review

import com.tiixel.periodictableprofessor.domain.card.interactor.CardInteractor
import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.presentation.review.model.CountsModel
import com.tiixel.periodictableprofessor.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReviewActionProcessor @Inject constructor(
    private val elementInteractor: ElementInteractor,
    private val cardInteractor: CardInteractor,
    private val schedulerProvider: BaseSchedulerProvider
) {

    private val loadNextCardProcessor = ObservableTransformer<ReviewAction.LoadNextCard, ReviewResult> { actions ->
        actions.switchMap { action ->
            when (action.newCard) {
                true -> cardInteractor.getNewCardForReview()
                false -> cardInteractor.getNextCardForReview(action.dueSoonOnly)
            }.flatMap { elementInteractor.getElement(it.item.itemId).zipWith(Single.just(it.face)) }
                .toObservable()
                .map { ReviewResult.LoadNextCardResult.Success(it.first, it.second) }
                .cast(ReviewResult.LoadNextCardResult::class.java)
                .onErrorReturn { ReviewResult.LoadNextCardResult.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(ReviewResult.LoadNextCardResult.InFlight)
        }
    }

    private val reviewCardProcessor = ObservableTransformer<ReviewAction.ReviewCard, ReviewResult> { actions ->
        actions.flatMap { action ->
            cardInteractor.reviewCard(action.freshReview)
                .toObservable<ReviewResult.ReviewCardResult>()
                .map { ReviewResult.ReviewCardResult.Success }
                .cast(ReviewResult.ReviewCardResult::class.java)
                .onErrorReturn { ReviewResult.ReviewCardResult.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(ReviewResult.ReviewCardResult.InFlight)
        }
    }

    private val getCountsProcessor = ObservableTransformer<ReviewAction.GetCounts, ReviewResult> { actions ->
        actions.flatMap { _ ->
            cardInteractor.countCardsDueSoon(Date())
                .zipWith(cardInteractor.countCardsDueToday(Date()), { w, v -> CountsModel(dueSoon = w, dueToday = v) })
                .zipWith(cardInteractor.countCardsNewOnDay(Date()), { model, v -> model.copy(newToday = v) })
                .toObservable()
                .zipWith(cardInteractor.getNextReviewDate().toObservable().materialize(), { model, v ->
                    model.copy(nextReviewTimer = v.value)
                })
                .map {
                    ReviewResult.GetCountsResult.Success(
                        dueSoon = it.dueSoon,
                        dueToday = it.dueToday,
                        new = it.newToday,
                        nextReviewTimer = timer(it.nextReviewTimer, Date())
                    )
                }
                .cast(ReviewResult.GetCountsResult::class.java)
                .onErrorReturn { ReviewResult.GetCountsResult.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(ReviewResult.GetCountsResult.InFlight)
        }
    }

    private val checkCardProcessor = ObservableTransformer<ReviewAction.CheckCard, ReviewResult> { actions ->
        actions.flatMap {
            Observable.just(ReviewResult.CheckCardResult)
        }
    }

    internal val actionProcessor = ObservableTransformer<ReviewAction, ReviewResult> { action ->
        action.publish { shared ->
            shared.ofType(ReviewAction.LoadNextCard::class.java)
                .compose(loadNextCardProcessor)
                .mergeWith(
                    shared.ofType(ReviewAction.ReviewCard::class.java).compose(reviewCardProcessor)
                )
                .mergeWith(
                    shared.ofType(ReviewAction.CheckCard::class.java).compose(checkCardProcessor)
                )
                .mergeWith(
                    shared.ofType(ReviewAction.GetCounts::class.java).compose(getCountsProcessor)
                )
                .mergeWith(shared.filter { action ->
                    action !is ReviewAction.LoadNextCard
                        && action !is ReviewAction.ReviewCard
                        && action !== ReviewAction.CheckCard
                        && action !== ReviewAction.GetCounts
                }.flatMap { action ->
                    Observable.error<ReviewResult>(IllegalArgumentException("Unknown action type: $action"))
                })
        }
    }

    private fun timer(date: Date?, ref: Date): List<Int>? {
        if (date == null) return null

        val millis = date.time - ref.time

        val days = TimeUnit.MILLISECONDS.toDays(millis)
        val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

        return listOf(days.toInt(), hours.toInt(), minutes.toInt(), seconds.toInt())
    }
}