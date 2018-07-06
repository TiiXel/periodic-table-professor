package com.tiixel.periodictableprofessor.presentation.review

import android.arch.lifecycle.ViewModel
import com.tiixel.periodictableprofessor.domain.exception.NoCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsDueSoonException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import com.tiixel.periodictableprofessor.presentation.base.MviViewModel
import com.tiixel.periodictableprofessor.presentation.review.model.CardModel
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
            .concatMap { Observable.just(it, ReviewAction.GetCounts) } // TODO: Is this working this time?
            .compose(actionProcessor.actionProcessor)
            .scan(ReviewViewState.init(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: ReviewIntent): ReviewAction {
        return when (intent) {
            is ReviewIntent.InitialIntent -> ReviewAction.LoadNextCard(intent.newCard, intent.dueSoonOnly)
            is ReviewIntent.LoadNextCardIntent -> ReviewAction.LoadNextCard(intent.newCard, intent.dueSoonOnly)
            is ReviewIntent.ReviewCardIntent -> ReviewAction.ReviewCard(
                intent.element,
                intent.performance
            )
            is ReviewIntent.CheckCardIntent -> ReviewAction.CheckCard
        }
    }

    companion object {

        private val reducer = BiFunction { previousState: ReviewViewState, result: ReviewResult ->
            when (result) {
                is ReviewResult.LoadNextCardResult -> {
                    when (result) {
                        is ReviewResult.LoadNextCardResult.Success -> {
                            previousState.copy(
                                loadingInProgress = false,
                                loadingFailedCause = null,
                                card = result.card,
                                showCheckButtonOverPerformance = true
                            )
                        }
                        is ReviewResult.LoadNextCardResult.Failure -> {
                            when (result.error) {
                                is NoNextReviewException -> {
                                }
                                is NoCardsAreNewException -> {
                                }
                                is NoCardsDueSoonException -> {
                                }
                                else -> throw result.error
                            }
                            previousState.copy(
                                loadingInProgress = false,
                                loadingFailedCause = result.error as Exception,
                                card = null,
                                showCheckButtonOverPerformance = false
                            )
                        }
                        is ReviewResult.LoadNextCardResult.InFlight -> {
                            previousState.copy(
                                loadingInProgress = true, showCheckButtonOverPerformance = true
                            )
                        }
                    }
                }
                is ReviewResult.ReviewCardResult -> {
                    when (result) {
                        is ReviewResult.ReviewCardResult.Success -> {
                            previousState.copy(
                                reviewingFailed = false
                            )
                        }
                        is ReviewResult.ReviewCardResult.Failure -> {
                            previousState.copy(reviewingFailed = true)
                            throw result.error
                        }
                        is ReviewResult.ReviewCardResult.InFlight -> {
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
                            throw result.error
                        }
                        is ReviewResult.GetCountsResult.InFlight -> {
                            previousState
                        }
                    }
                }
                is ReviewResult.CheckCardResult -> {
                    previousState.copy(
                        showCheckButtonOverPerformance = false,
                        card = CardModel.reveal(previousState.card!!)
                    )
                }
            }
        }
    }
}