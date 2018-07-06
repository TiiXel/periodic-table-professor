package com.tiixel.periodictableprofessor.presentation.study.model

import java.util.Date

data class CountsModel(
    val dueSoon: Int = 0,
    val dueToday: Int = 0,
    val newToday: Int = 0,
    val nextReviewTimer: Date? = null
)