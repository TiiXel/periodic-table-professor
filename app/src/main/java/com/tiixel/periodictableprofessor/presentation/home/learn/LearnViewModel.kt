package com.tiixel.periodictableprofessor.presentation.home.learn

import android.arch.lifecycle.ViewModel
import com.tiixel.periodictableprofessor.presentation.base.MviViewModel
import com.tiixel.periodictableprofessor.presentation.study.StudyAction
import com.tiixel.periodictableprofessor.presentation.study.StudyActionProcessor
import com.tiixel.periodictableprofessor.presentation.study.StudyResult
import com.tiixel.periodictableprofessor.util.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LearnViewModel @Inject constructor(
    private val actionProcessor: StudyActionProcessor
): ViewModel(), MviViewModel<LearnIntent, LearnViewState> {

    private val intentsSubject: PublishSubject<LearnIntent> = PublishSubject.create()
    private val statesObservable: Observable<LearnViewState> = compose()

    private val intentFilter: ObservableTransformer<LearnIntent, LearnIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(LearnIntent.InitialIntent::class.java).take(1),
                    shared.notOfType(LearnIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<LearnIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<LearnViewState> = statesObservable

    private fun compose(): Observable<LearnViewState> {
        return intentsSubject.compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessor.actionProcessor)
            .scan(LearnViewState.init(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: LearnIntent): StudyAction {
        return when (intent) {
            is LearnIntent.InitialIntent -> StudyAction.GetCounts
        }
    }

    companion object {

        private val reducer = BiFunction { previousState: LearnViewState, result: StudyResult ->
            when (result) {
                is StudyResult.LoadNextReviewResult -> {
                    previousState.copy()
                }
                is StudyResult.GetCountsResult -> {
                    previousState.copy()
                }
                else -> {
                    previousState.copy()
                }
            }
        }
    }
}