package com.tiixel.periodictableprofessor.presentation.review

import android.arch.lifecycle.ViewModel
import com.tiixel.periodictableprofessor.domain.Review
import com.tiixel.periodictableprofessor.domain.Reviewable
import com.tiixel.periodictableprofessor.domain.ReviewableFace
import com.tiixel.periodictableprofessor.domain.exception.NoNewReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewSoonException
import com.tiixel.periodictableprofessor.presentation.base.MviViewModel
import com.tiixel.periodictableprofessor.presentation.review.mapper.ElementMapper
import com.tiixel.periodictableprofessor.util.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ReviewViewModel @Inject constructor(
    private val actionProcessor: ReviewActionProcessor
) : ViewModel(), MviViewModel<ReviewIntent, ReviewViewState> {

    private val intentsSubjects: PublishSubject<ReviewIntent> = PublishSubject.create()
    private val statesObservable: Observable<ReviewViewState> = compose()

    private val intentFilter: ObservableTransformer<ReviewIntent, ReviewIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(ReviewIntent.InitialIntent::class.java).take(1),
                    shared.notOfType(ReviewIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<ReviewIntent>) {
        intents.subscribe(intentsSubjects)
    }

    override fun states(): Observable<ReviewViewState> = statesObservable

    private fun compose(): Observable<ReviewViewState> {
        return intentsSubjects.compose(intentFilter)
            .map(this::actionFromIntent)
            .concatMap { Observable.just(it, ReviewAction.GetCounts) }
            .compose(actionProcessor.actionProcessor)
            .scan(ReviewViewState.init(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: ReviewIntent): ReviewAction {
        return when (intent) {
            is ReviewIntent.InitialIntent -> ReviewAction.LoadNext(intent.newCard, intent.dueSoonOnly)
            is ReviewIntent.LoadNextIntent -> ReviewAction.LoadNext(intent.newCard, intent.dueSoonOnly)
            is ReviewIntent.Review_Intent -> ReviewAction.Review(
                Review.FreshReview(
                    item = Reviewable(intent.element),
                    face = intent.face,
                    performance = intent.performance
                )
            )
            is ReviewIntent.CheckIntent -> ReviewAction.Check
        }
    }

    companion object {

        private val reducer = BiFunction { previousState: ReviewViewState, result: ReviewResult ->
            when (result) {
                is ReviewResult.LoadNextReviewResult -> {
                    when (result) {
                        is ReviewResult.LoadNextReviewResult.Success -> {
                            previousState.copy(
                                loadingInProgress = false,
                                loadingFailedCause = null,
                                element = ElementMapper.fromDomain(result.element),
                                showCheckButtonOverPerformance = true,
                                isNameVisible = result.face == ReviewableFace.NAME,
                                isSymbolVisible = result.face == ReviewableFace.SYMBOL,
                                isUserNoteVisible = false,
                                isNumberVisible = result.face == ReviewableFace.NUMBER,
                                isPictureVisible = result.face == ReviewableFace.PICTURE,
                                isPhraseVisible = false,
                                isTablePositionVisible = false
                            )
                        }
                        is ReviewResult.LoadNextReviewResult.Failure -> {
                            when (result.error) {
                                is NoNextReviewException -> {
                                }
                                is NoNewReviewException -> {
                                }
                                is NoNextReviewSoonException -> {
                                }
                                else -> throw result.error
                            }
                            previousState.copy(
                                loadingInProgress = false,
                                loadingFailedCause = result.error as Exception,
                                element = null,
                                showCheckButtonOverPerformance = false
                            )
                        }
                        is ReviewResult.LoadNextReviewResult.InFlight -> {
                            previousState.copy(
                                loadingInProgress = true, showCheckButtonOverPerformance = true
                            )
                        }
                    }
                }
                is ReviewResult.Review_Result -> {
                    when (result) {
                        is ReviewResult.Review_Result.Success -> {
                            previousState.copy(
                                reviewingFailed = false
                            )
                        }
                        is ReviewResult.Review_Result.Failure -> {
                            previousState.copy(reviewingFailed = true)
                            throw result.error
                        }
                        is ReviewResult.Review_Result.InFlight -> {
                            previousState.copy(showCheckButtonOverPerformance = false)
                        }
                    }
                }
                is ReviewResult.GetCountsResult -> {
                    when (result) {
                        is ReviewResult.GetCountsResult.Success -> {
                            previousState.copy(
                                newCardCount = result.new,
                                dueSoonCount = result.dueSoon,
                                dueTodayCount = result.dueToday,
                                nextReviewTimer = result.nextReviewTimer
                            )
                        }
                        is ReviewResult.GetCountsResult.Failure -> {
                            when (result.error) {
                                is NoNextReviewException -> {
                                }
                                else -> throw result.error
                            }
                            previousState
                        }
                        is ReviewResult.GetCountsResult.InFlight -> {
                            previousState
                        }
                    }
                }
                is ReviewResult.CheckResult -> {
                    previousState.copy(
                        showCheckButtonOverPerformance = false,
                        isNumberVisible = true,
                        isSymbolVisible = true,
                        isNameVisible = true,
                        isTablePositionVisible = true,
                        isPictureVisible = true,
                        isPhraseVisible = true,
                        isUserNoteVisible = true
                    )
                }
            }
        }
    }
}