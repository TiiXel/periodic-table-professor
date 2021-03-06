package com.tiixel.periodictableprofessor.presentation.elementlist

import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.presentation.elementlist.model.ElementModel
import com.tiixel.periodictableprofessor.util.schedulers.BaseSchedulerProvider
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class ElementListActionProcessor @Inject constructor(
    private val elementInteractor: ElementInteractor,
    private val schedulerProvider: BaseSchedulerProvider
) {

    private val loadElementsProcessor =
        ObservableTransformer<ElementListAction.LoadElementList, ElementListResult> { actions ->
            actions.switchMap { action ->
                elementInteractor.getElements()
                    .toObservable()
                    .map { ElementModel.listFromDomain(it, action.dataPoint) }
                    .map { models -> ElementListResult.LoadElementListResult.Success(models) }
                    .cast(ElementListResult.LoadElementListResult::class.java)
                    .onErrorReturn { ElementListResult.LoadElementListResult.Failure(it) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(ElementListResult.LoadElementListResult.InFlight)
            }
        }

    internal val actionProcessor =
        ObservableTransformer<ElementListAction, ElementListResult> { action ->
            action.publish { shared ->
                shared.ofType(ElementListAction.LoadElementList::class.java).compose(loadElementsProcessor)
            }
        }
}