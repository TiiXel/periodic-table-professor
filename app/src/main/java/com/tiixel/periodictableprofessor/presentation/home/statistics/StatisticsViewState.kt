package com.tiixel.periodictableprofessor.presentation.home.statistics

import com.tiixel.periodictableprofessor.presentation.base.MviViewState
import java.util.Date

data class StatisticsViewState(
    val loadingInProgress: Boolean,
    val loadingFailedCause: Throwable?,
    val dataItemsNewPerDay: Map<Date, Int>?
) : MviViewState {

    companion object {

        fun idle(): StatisticsViewState {
            return StatisticsViewState(
                loadingInProgress = true,
                loadingFailedCause = null,
                dataItemsNewPerDay = null
            )
        }
    }
}