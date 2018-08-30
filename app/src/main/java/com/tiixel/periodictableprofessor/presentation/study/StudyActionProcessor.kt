package com.tiixel.periodictableprofessor.presentation.study

import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.domain.review.interactor.ReviewInteractor
import com.tiixel.periodictableprofessor.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import org.apache.commons.lang3.time.DateUtils
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StudyActionProcessor @Inject constructor(
    private val elementInteractor: ElementInteractor,
    private val reviewInteractor: ReviewInteractor,
    private val schedulerProvider: BaseSchedulerProvider
) {

    private val loadReviewableProcessor = ObservableTransformer<StudyAction.LoadNext, StudyResult> { actions ->
        actions.switchMap { action ->
            when (action.newCard) {
                true -> reviewInteractor.getReviewForNew()
                false -> reviewInteractor.getReviewForNext(action.dueSoonOnly)
            }.flatMap { elementInteractor.getElement(it.item.itemId).zipWith(Single.just(it.face)) }
                .toObservable()
                .map { StudyResult.LoadNextReviewResult.Success(it.first, it.second) }
                .cast(StudyResult.LoadNextReviewResult::class.java)
                .onErrorReturn { StudyResult.LoadNextReviewResult.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(StudyResult.LoadNextReviewResult.InFlight)
        }
    }

    private val reviewProcessor = ObservableTransformer<StudyAction.Review, StudyResult> { actions ->
        actions.switchMap { action ->
            reviewInteractor.review(action.freshReview)
                .toObservable<StudyResult.ReviewResult>()
                .map { StudyResult.ReviewResult.Success }
                .cast(StudyResult.ReviewResult::class.java)
                .onErrorReturn { StudyResult.ReviewResult.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(StudyResult.ReviewResult.InFlight)
        }
    }

    private val getCountsProcessor = ObservableTransformer<StudyAction.GetCounts, StudyResult> { actions ->
        actions.flatMap { _ ->
            reviewInteractor.countReviewablesNewPerPeriod(Calendar.DATE)
                .zipWith(reviewInteractor.countReviewsDueSoon(Date()))
                .zipWith(reviewInteractor.countReviewsDuePerPeriod(Calendar.DATE))
                .zipWith(reviewInteractor.getNextReviewDate())
                .toObservable()
                .map {
                    StudyResult.GetCountsResult.Success(
                        new = it.first.first.first.getOrElse(DateUtils.truncate(Date(), Calendar.DATE)) { 0 },
                        dueSoon = it.first.first.second,
                        dueToday = it.first.second.getOrElse(DateUtils.truncate(Date(), Calendar.DATE)) { 0 },
                        nextReviewTimer = timer(it.second, Date())
                    )
                }
                .cast(StudyResult.GetCountsResult::class.java)
                .onErrorReturn { StudyResult.GetCountsResult.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(StudyResult.GetCountsResult.InFlight)
        }
    }

    private val checkCardProcessor = ObservableTransformer<StudyAction.Check, StudyResult> { actions ->
        actions.flatMap { Observable.just(StudyResult.CheckResult) }
    }

    internal val actionProcessor = ObservableTransformer<StudyAction, StudyResult> { action ->
        action.publish { shared ->
            shared.ofType(StudyAction.LoadNext::class.java).compose(loadReviewableProcessor)
                .mergeWith(shared.ofType(StudyAction.Review::class.java).compose(reviewProcessor))
                .mergeWith(shared.ofType(StudyAction.Check::class.java).compose(checkCardProcessor))
                .mergeWith(shared.ofType(StudyAction.GetCounts::class.java).compose(getCountsProcessor))
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