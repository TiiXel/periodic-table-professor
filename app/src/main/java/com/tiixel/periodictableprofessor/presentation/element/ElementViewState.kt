package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.presentation.base.MviViewState
import com.tiixel.periodictableprofessor.presentation.element.model.ElementModel

data class ElementViewState(
    val element: ElementModel?,
    val loadingInProgress: Boolean,
    val loadingFailedCause: Throwable?
) : MviViewState {

    companion object {

        fun idle(): ElementViewState {
            return ElementViewState(
                element = null,
                loadingInProgress = false,
                loadingFailedCause = null
            )
        }
    }
}