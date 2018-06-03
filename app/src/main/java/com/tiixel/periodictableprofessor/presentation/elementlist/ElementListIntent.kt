package com.tiixel.periodictableprofessor.presentation.elementlist

import com.tiixel.periodictableprofessor.presentation.base.MviIntent
import com.tiixel.periodictableprofessor.presentation.elementlist.model.ElementModel

sealed class ElementListIntent : MviIntent {

    object InitialIntent : ElementListIntent()

    data class LoadElementListIntent(val dataPoint: ElementModel.Companion.OneLineDataPoint) : ElementListIntent()
}