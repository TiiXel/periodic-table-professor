package com.tiixel.periodictableprofessor.presentation.study

import android.arch.lifecycle.ViewModel
import com.tiixel.periodictableprofessor.domain.Review
import com.tiixel.periodictableprofessor.domain.Reviewable
import com.tiixel.periodictableprofessor.domain.ReviewableFace
import com.tiixel.periodictableprofessor.domain.exception.NoNewReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewSoonException
import com.tiixel.periodictableprofessor.presentation.base.MviViewModel
import com.tiixel.periodictableprofessor.presentation.study.mapper.ElementMapper
import com.tiixel.periodictableprofessor.util.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class StudyViewModel @Inject constructor(
    private val actionProcessor: StudyActionProcessor
) : ViewModel(), MviViewModel<StudyIntent, StudyViewState> {

    private val intentsSubjects: PublishSubject<StudyIntent> = PublishSubject.create()
    private val statesObservable: Observable<StudyViewState> = compose()

    private val intentFilter: ObservableTransformer<StudyIntent, StudyIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(StudyIntent.InitialIntent::class.java).take(1),
                    shared.notOfType(StudyIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<StudyIntent>) {
        intents.subscribe(intentsSubjects)
    }

    override fun states(): Observable<StudyViewState> = statesObservable

    private fun compose(): Observable<StudyViewState> {
        return intentsSubjects.compose(intentFilter)
            .map(this::actionFromIntent)
            .concatMap { Observable.just(it, StudyAction.GetCounts) }
            .compose(actionProcessor.actionProcessor)
            .scan(StudyViewState.init(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: StudyIntent): StudyAction {
        return when (intent) {
            is StudyIntent.InitialIntent -> StudyAction.LoadNext(intent.newCard, intent.dueSoonOnly)
            is StudyIntent.LoadNextIntent -> StudyAction.LoadNext(intent.newCard, intent.dueSoonOnly)
            is StudyIntent.ReviewIntent -> StudyAction.Review(
                Review.FreshReview(
                    item = Reviewable(intent.element),
                    face = intent.face,
                    performance = intent.performance
                )
            )
            is StudyIntent.CheckIntent -> StudyAction.Check
        }
    }

    companion object {

        private val reducer = BiFunction { previousState: StudyViewState, result: StudyResult ->
            when (result) {
                is StudyResult.LoadNextReviewResult -> {
                    when (result) {
                        is StudyResult.LoadNextReviewResult.Success -> {
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
                        is StudyResult.LoadNextReviewResult.Failure -> {
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
                        is StudyResult.LoadNextReviewResult.InFlight -> {
                            previousState.copy(
                                loadingInProgress = true, showCheckButtonOverPerformance = true
                            )
                        }
                    }
                }
                is StudyResult.ReviewResult -> {
                    when (result) {
                        is StudyResult.ReviewResult.Success -> {
                            previousState.copy(
                                reviewingFailed = false
                            )
                        }
                        is StudyResult.ReviewResult.Failure -> {
                            previousState.copy(reviewingFailed = true)
                            throw result.error
                        }
                        is StudyResult.ReviewResult.InFlight -> {
                            previousState.copy(showCheckButtonOverPerformance = false)
                        }
                    }
                }
                is StudyResult.GetCountsResult -> {
                    when (result) {
                        is StudyResult.GetCountsResult.Success -> {
                            previousState.copy(
                                newCardCount = result.new,
                                dueSoonCount = result.dueSoon,
                                dueTodayCount = result.dueToday,
                                nextReviewTimer = result.nextReviewTimer
                            )
                        }
                        is StudyResult.GetCountsResult.Failure -> {
                            when (result.error) {
                                is NoNextReviewException -> {
                                }
                                else -> throw result.error
                            }
                            previousState
                        }
                        is StudyResult.GetCountsResult.InFlight -> {
                            previousState
                        }
                    }
                }
                is StudyResult.CheckResult -> {
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