package com.tiixel.periodictableprofessor.domain

import org.apache.commons.lang3.time.DateUtils
import java.util.Date

data class ReviewData(
    val element: Byte,
    private val _difficulty: Float,
    val reviewDate: Date,
    val nextInterval: Long,
    val performance: Card.Companion.Performance? = null
) {
    val difficulty: Float = _difficulty ?: 0.3f
    val nextDateOverdue: Float?
        get() {
            return (Date().time - reviewDate.time).toFloat() / nextInterval
        }
    val nextDate = Date(reviewDate.time + nextInterval)

    fun isDueSoon(reference: Date): Boolean {
        return nextDateOverdue?.let { it >= 0.75 } ?: false
    }

    fun isDueOnDay(reference: Date): Boolean {
        return DateUtils.isSameDay(reference, nextDate)
    }
}