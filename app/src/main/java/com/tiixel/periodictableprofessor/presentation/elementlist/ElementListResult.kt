package com.tiixel.periodictableprofessor.presentation.elementlist

import com.tiixel.periodictableprofessor.presentation.base.MviResult
import com.tiixel.periodictableprofessor.presentation.elementlist.model.ElementModel

sealed class ElementListResult : MviResult {

    sealed class LoadElementListResult : ElementListResult() {

        data class Success(val elements: List<ElementModel>) : LoadElementListResult()

        data class Failure(val error: Throwable) : LoadElementListResult()

        object InFlight : LoadElementListResult()
    }
}