package com.tiixel.periodictableprofessor.factory

import com.tiixel.periodictableprofessor.domain.Card
import com.tiixel.periodictableprofessor.domain.ReviewData
import com.tiixel.periodictableprofessor.domain.algorithm.Sm2Plus
import java.util.Date

object ReviewDataFactory {

    fun makePerfectReviewHistory(size: Int): MutableList<ReviewData> {
        val log: MutableList<ReviewData> = emptyList<ReviewData>().toMutableList()
        if (size == 0) return log

        log.add(Sm2Plus.defaultReview(1, Date(0)))

        repeat(size - 1) {
            val nextDate = Date(log.last().nextDate.time)
            log.add(Sm2Plus.compute(log.last(), Card.Companion.Performance.EASY, nextDate))
        }

        return log
    }

    fun makeReviewHistoryForElements(elements: List<Byte>): MutableList<ReviewData> {
        val log: MutableList<ReviewData> = emptyList<ReviewData>().toMutableList()

        elements.forEach {
            log.add(Sm2Plus.defaultReview(it, Date(it.toLong())))
        }

        return log
    }
}