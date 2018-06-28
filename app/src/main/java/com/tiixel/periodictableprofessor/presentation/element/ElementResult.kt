package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.presentation.base.MviResult

sealed class ElementResult : MviResult {

    sealed class LoadElementResult : ElementResult() {

        data class Success(val element: Element) : LoadElementResult()

        data class Failure(val error: Throwable) : LoadElementResult()

        object InFlight : LoadElementResult()
    }
}