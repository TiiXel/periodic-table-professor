package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.presentation.base.MviIntent

sealed class ElementIntent : MviIntent {

    object InitialIntent : ElementIntent()
}