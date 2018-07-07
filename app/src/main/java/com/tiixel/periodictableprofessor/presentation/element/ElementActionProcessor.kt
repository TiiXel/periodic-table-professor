package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.presentation.element.model.ElementModel
import com.tiixel.periodictableprofessor.util.schedulers.BaseSchedulerProvider
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class ElementActionProcessor @Inject constructor(
    private val elementInteractor: ElementInteractor,
    private val schedulerProvider: BaseSchedulerProvider
) {

    private val loadElementProcessor =
        ObservableTransformer<ElementAction.LoadElement, ElementResult> { actions ->
            actions.switchMap { action ->
                elementInteractor.getElement(action.element)
                    .toObservable()
                    .map { ElementModel.fromDomain(it) }
                    .map { ElementResult.LoadElementResult.Success(it) }
                    .cast(ElementResult.LoadElementResult::class.java)
                    .onErrorReturn { ElementResult.LoadElementResult.Failure(it) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(ElementResult.LoadElementResult.InFlight)
            }
        }

    internal val actionProcessor =
        ObservableTransformer<ElementAction, ElementResult> { action ->
            action.publish { shared ->
                shared.ofType(ElementAction.LoadElement::class.java).compose(loadElementProcessor)
            }
        }
}