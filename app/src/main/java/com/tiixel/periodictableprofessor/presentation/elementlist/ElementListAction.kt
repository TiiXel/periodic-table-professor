package com.tiixel.periodictableprofessor.presentation.elementlist

import com.tiixel.periodictableprofessor.presentation.base.MviAction
import com.tiixel.periodictableprofessor.presentation.elementlist.model.ElementModel

sealed class ElementListAction : MviAction {

    data class LoadElementList(val dataPoint: ElementModel.Companion.OneLineDataPoint) : ElementListAction()
}