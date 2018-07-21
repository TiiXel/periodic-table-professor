package com.tiixel.periodictableprofessor.presentation.home.statistics

import com.tiixel.periodictableprofessor.presentation.base.MviAction

sealed class StatisticsAction : MviAction {

    data class LoadStatsAction(val filterType: Int?) : StatisticsAction()
}