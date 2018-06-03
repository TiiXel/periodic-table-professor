package com.tiixel.periodictableprofessor.presentation.elementlist

import com.tiixel.periodictableprofessor.presentation.base.MviViewState
import com.tiixel.periodictableprofessor.presentation.elementlist.model.ElementModel

data class ElementListViewState(
    val elements: List<ElementModel>,
    val selectedData: ElementModel.Companion.OneLineDataPoint,
    val loadingInProgress: Boolean,
    val loadingFailed: Boolean
) : MviViewState {

    companion object {

        fun idle(): ElementListViewState {
            return ElementListViewState(
                elements = emptyList(),
                selectedData = ElementModel.Companion.OneLineDataPoint.NAME,
                loadingFailed = false,
                loadingInProgress = false
            )
        }
    }
}