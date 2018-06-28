package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.presentation.base.MviViewState

data class ElementViewState(
    val element: Element?,
    val loadingInProgress: Boolean,
    val loadingFailed: Boolean
) : MviViewState {

    companion object {

        fun idle(): ElementViewState {
            return ElementViewState(
                element = null,
                loadingInProgress = false,
                loadingFailed = false
            )
        }
    }
}