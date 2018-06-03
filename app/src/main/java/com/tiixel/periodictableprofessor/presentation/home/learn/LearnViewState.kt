package com.tiixel.periodictableprofessor.presentation.home.learn

import com.tiixel.periodictableprofessor.presentation.base.MviViewState

data class LearnViewState(
    val a: Int
) : MviViewState {

    companion object {

        fun init() : LearnViewState {
            return LearnViewState(1)
        }
    }
}