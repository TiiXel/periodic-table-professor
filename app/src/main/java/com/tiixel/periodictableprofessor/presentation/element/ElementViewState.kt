package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.presentation.base.MviViewState

data class ElementViewState(
    val isLoading: Boolean
) : MviViewState {

    companion object {

        fun idle(): ElementViewState {
            return ElementViewState(
                true
            )
        }
    }
}