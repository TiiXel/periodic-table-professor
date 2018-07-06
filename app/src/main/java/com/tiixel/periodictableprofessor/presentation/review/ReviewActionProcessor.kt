package com.tiixel.periodictableprofessor.presentation.review

import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.domain.review.interactor.ReviewInteractor
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
    private val reviewInteractor: ReviewInteractor,
    private val schedulerProvider: BaseSchedulerProvider
) {

    private val loadReviewableProcessor = ObservableTransformer<ReviewAction.LoadNext, ReviewResult> { actions ->
        actions.switchMap { action ->
            when (action.newCard) {
                true -> reviewInteractor.getReviewForNew()
                false -> reviewInteractor.getReviewForNext(action.dueSoonOnly)
            }.flatMap { elementInteractor.getElement(it.item.itemId).zipWith(Single.just(it.face)) }
                .toObservable()
                .map { ReviewResult.LoadNextReviewResult.Success(it.first, it.second) }
                .cast(ReviewResult.LoadNextReviewResult::class.java)
                .onErrorReturn { ReviewResult.LoadNextReviewResult.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(ReviewResult.LoadNextReviewResult.InFlight)
        }
    }

    private val reviewProcessor = ObservableTransformer<ReviewAction.Review, ReviewResult> { actions ->
        actions.flatMap { action ->
            reviewInteractor.review(action.freshReview)
                .toObservable<ReviewResult.Review_Result>()
                .map { ReviewResult.Review_Result.Success }
                .cast(ReviewResult.Review_Result::class.java)
                .onErrorReturn { ReviewResult.Review_Result.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(ReviewResult.Review_Result.InFlight)
        }
    }

    private val getCountsProcessor = ObservableTransformer<ReviewAction.GetCounts, ReviewResult> { actions ->
        actions.flatMap { _ ->
            reviewInteractor.countReviewablesNewOnDay(Date())
                .zipWith(reviewInteractor.countReviewsDueSoon(Date()))
                .zipWith(reviewInteractor.countReviewsDueOnDay(Date()))
                .zipWith(reviewInteractor.getNextReviewDate())
                .toObservable()
                .map {
                    ReviewResult.GetCountsResult.Success(
                        new = it.first.first.first,
                        dueSoon = it.first.first.second,
                        dueToday = it.first.second,
                        nextReviewTimer = timer(it.second, Date())
                    )
                }
                .cast(ReviewResult.GetCountsResult::class.java)
                .onErrorReturn { ReviewResult.GetCountsResult.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(ReviewResult.GetCountsResult.InFlight)
        }
    }

    private val checkCardProcessor = ObservableTransformer<ReviewAction.Check, ReviewResult> { actions ->
        actions.flatMap { Observable.just(ReviewResult.CheckResult) }
    }

    internal val actionProcessor = ObservableTransformer<ReviewAction, ReviewResult> { action ->
        action.publish { shared ->
            shared.ofType(ReviewAction.LoadNext::class.java).compose(loadReviewableProcessor)
                .mergeWith(shared.ofType(ReviewAction.Review::class.java).compose(reviewProcessor))
                .mergeWith(shared.ofType(ReviewAction.Check::class.java).compose(checkCardProcessor))
                .mergeWith(shared.ofType(ReviewAction.GetCounts::class.java).compose(getCountsProcessor))
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