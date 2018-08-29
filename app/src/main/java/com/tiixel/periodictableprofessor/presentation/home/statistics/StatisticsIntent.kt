package com.tiixel.periodictableprofessor.presentation.home.statistics

import com.tiixel.periodictableprofessor.presentation.base.MviIntent

sealed class StatisticsIntent : MviIntent {

    object InitialIntent : StatisticsIntent()
}