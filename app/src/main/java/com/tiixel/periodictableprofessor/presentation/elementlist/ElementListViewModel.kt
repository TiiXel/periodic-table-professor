package com.tiixel.periodictableprofessor.presentation.elementlist

import android.arch.lifecycle.ViewModel
import com.tiixel.periodictableprofessor.presentation.base.MviViewModel
import com.tiixel.periodictableprofessor.presentation.elementlist.model.ElementModel
import com.tiixel.periodictableprofessor.util.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ElementListViewModel @Inject constructor(
    private val actionProcessor: ElementListActionProcessor
) : ViewModel(), MviViewModel<ElementListIntent, ElementListViewState> {

    private val intentsSubjects: PublishSubject<ElementListIntent> = PublishSubject.create()
    private val statesObservable: Observable<ElementListViewState> = compose()

    private val intentFilter: ObservableTransformer<ElementListIntent, ElementListIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(ElementListIntent.InitialIntent::class.java).take(1),
                    shared.notOfType(ElementListIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<ElementListIntent>) {
        intents.subscribe(intentsSubjects)
    }

    override fun states(): Observable<ElementListViewState> = statesObservable

    private fun compose(): Observable<ElementListViewState> {
        return intentsSubjects
            .compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessor.actionProcessor)
            .scan(
                ElementListViewState.idle(),
                reducer
            )
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: ElementListIntent): ElementListAction {
        return when (intent) {
            is ElementListIntent.InitialIntent -> ElementListAction.LoadElementList(ElementModel.Companion.OneLineDataPoint.NAME)
            is ElementListIntent.LoadElementListIntent -> ElementListAction.LoadElementList(intent.dataPoint)
        }
    }

    companion object {

        private val reducer = BiFunction { previousState: ElementListViewState, result: ElementListResult ->
            when (result) {
                is ElementListResult.LoadElementListResult.Success -> {
                    previousState.copy(
                        loadingInProgress = false,
                        elements = result.elements
                    )
                }
                is ElementListResult.LoadElementListResult.Failure -> {
                    previousState.copy(
                        loadingInProgress = false,
                        loadingFailed = true
                    )
                    throw result.error
                }
                is ElementListResult.LoadElementListResult.InFlight -> {
                    previousState.copy(
                        loadingInProgress = true
                    )
                }
            }
        }
    }
}