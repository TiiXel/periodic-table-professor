package com.tiixel.periodictableprofessor.presentation.home.learn

import com.tiixel.periodictableprofessor.presentation.base.MviIntent

sealed class LearnIntent  : MviIntent {

    object InitialIntent : LearnIntent()
}