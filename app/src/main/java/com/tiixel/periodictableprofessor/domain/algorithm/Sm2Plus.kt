package com.tiixel.periodictableprofessor.domain.algorithm

import com.tiixel.periodictableprofessor.domain.Card
import com.tiixel.periodictableprofessor.domain.ReviewData
import java.util.Date
import kotlin.math.max
import kotlin.math.min

object Sm2Plus {

    fun defaultReview(element: Byte, time: Date) =
        ReviewData(element, 0.3f, time, 10 * 60 * 1000L, Card.Companion.Performance.FAILED)

    /**
     * http://www.blueraja.com/blog/477/a-better-spaced-repetition-learning-algorithm-sm2
     */
    fun compute(lastLog: ReviewData, performance: Card.Companion.Performance, time: Date): ReviewData {

        val percOverdue = (time.time - lastLog.reviewDate.time) / lastLog.nextInterval.toFloat()

        val corrOverdue = if (performance == Card.Companion.Performance.FAILED) 1f else min(2f, percOverdue)

        val newDifficulty =
            min(max(lastLog.difficulty + corrOverdue * 1 / 17f * (8 - 9f * performanceRating(performance)), 0f), 1f)

        val difficultyWeight = 3 - 1.7f * newDifficulty

        val intervalCorrector =
            if (performance == Card.Companion.Performance.FAILED) 1 / difficultyWeight / difficultyWeight
            else 1 + (difficultyWeight - 1) * corrOverdue

        val nextInterval = (lastLog.nextInterval * intervalCorrector).toLong()

        return ReviewData(
            element = lastLog.element,
            _difficulty = newDifficulty,
            reviewDate = time,
            nextInterval = nextInterval,
            performance = performance
        )
    }

    private fun performanceRating(performance: Card.Companion.Performance): Float {
        return when (performance) {
            Card.Companion.Performance.FAILED -> 0f
            Card.Companion.Performance.HARD -> 0.7f
            Card.Companion.Performance.MEDIUM -> 0.8f
            Card.Companion.Performance.EASY -> 1f
        }
    }
}