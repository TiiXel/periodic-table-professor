package com.tiixel.periodictableprofessor.ui.statistics.model

import java.util.Date

data class ChartModel(
    val title: String,
    val data: Map<Date, Int>?,
    val color: Int
)