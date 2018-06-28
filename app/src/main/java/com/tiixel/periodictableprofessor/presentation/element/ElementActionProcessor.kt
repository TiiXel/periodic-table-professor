package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class ElementActionProcessor @Inject constructor(
    private val schedulerProvider: BaseSchedulerProvider
) {

    internal val actionProcessor =
        ObservableTransformer<ElementAction, ElementResult> { action ->
            action.publish { shared ->
                // TODO: Process actions
                shared.flatMap { action ->
                    Observable.error<ElementResult>(IllegalArgumentException("Unknown action type: $action"))
                }
            }
        }
}