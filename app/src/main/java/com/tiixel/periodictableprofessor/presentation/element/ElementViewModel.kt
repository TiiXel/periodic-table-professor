package com.tiixel.periodictableprofessor.presentation.element

import android.arch.lifecycle.ViewModel
import com.tiixel.periodictableprofessor.presentation.base.MviViewModel
import com.tiixel.periodictableprofessor.util.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ElementViewModel @Inject constructor(
    private val actionProcessor: ElementActionProcessor
) : ViewModel(), MviViewModel<ElementIntent, ElementViewState> {

    private val intentsSubjects: PublishSubject<ElementIntent> = PublishSubject.create()
    private val statesObservables: Observable<ElementViewState> = compose()

    private val intentFilter: ObservableTransformer<ElementIntent, ElementIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(ElementIntent.InitialIntent::class.java).take(1),
                    shared.notOfType(ElementIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<ElementIntent>) {
        intents.subscribe(intentsSubjects)
    }

    override fun states(): Observable<ElementViewState> = statesObservables

    private fun compose(): Observable<ElementViewState> {
        return intentsSubjects
            .compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessor.actionProcessor)
            .scan(ElementViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: ElementIntent): ElementAction {
        TODO("Map actions to intents")
    }

    companion object {

        private val reducer = BiFunction { previousState: ElementViewState, result: ElementResult ->
            // TODO: Reduce results to view state
            return@BiFunction ElementViewState.idle()
        }
    }
}