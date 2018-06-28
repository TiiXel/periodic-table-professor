package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.presentation.base.MviIntent

sealed class ElementIntent : MviIntent {

    data class InitialIntent(val element: Byte) : ElementIntent()
}