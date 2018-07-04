package com.tiixel.periodictableprofessor.presentation.element

import com.tiixel.periodictableprofessor.presentation.base.MviAction

sealed class ElementAction : MviAction {

    data class LoadElement(val element: Byte) : ElementAction()
}