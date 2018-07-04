package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.presentation.base.MviResult
import com.tiixel.periodictableprofessor.presentation.element.model.ElementModel

sealed class ElementResult : MviResult {

    sealed class LoadElementResult : ElementResult() {

        data class Success(val element: ElementModel) : LoadElementResult()

        data class Failure(val error: Throwable) : LoadElementResult()

        object InFlight : LoadElementResult()
    }
}