package com.tiixel.periodictableprofessor.presentation.home.statistics

import com.tiixel.periodictableprofessor.presentation.base.MviResult
import java.util.Date

sealed class StatisticsResult : MviResult {

    sealed class LoadStatsResult : StatisticsResult() {

        data class Success(val itemsNewPerDay: Map<Date, Int>) : LoadStatsResult()

        data class Failure(val error: Throwable) : LoadStatsResult()

        object InFlight : LoadStatsResult()
    }
}