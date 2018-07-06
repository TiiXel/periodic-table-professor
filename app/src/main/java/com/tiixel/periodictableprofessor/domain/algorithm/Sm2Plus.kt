package com.tiixel.periodictableprofessor.domain.algorithm

import com.tiixel.periodictableprofessor.domain.ReviewData
import com.tiixel.periodictableprofessor.domain.ReviewPerformance
import java.util.Date
import kotlin.math.max
import kotlin.math.min

object Sm2Plus {

    fun defaultReview(element: Byte, time: Date) =
        ReviewData(element, 0.3f, time, 10 * 60 * 1000L, ReviewPerformance.FAILED)

    /**
     * http://www.blueraja.com/blog/477/a-better-spaced-repetition-learning-algorithm-sm2
     */
    fun compute(lastLog: ReviewData, performance: ReviewPerformance, time: Date): ReviewData {

        val percOverdue = (time.time - lastLog.reviewDate.time) / lastLog.nextInterval.toFloat()

        val corrOverdue = if (performance == ReviewPerformance.FAILED) 1f else min(2f, percOverdue)

        val newDifficulty =
            min(max(lastLog.difficulty + corrOverdue * 1 / 17f * (8 - 9f * performanceRating(performance)), 0f), 1f)

        val difficultyWeight = 3 - 1.7f * newDifficulty

        val intervalCorrector =
            if (performance == ReviewPerformance.FAILED) 1 / difficultyWeight / difficultyWeight
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

    private fun performanceRating(performance: ReviewPerformance): Float {
        return when (performance) {
            ReviewPerformance.FAILED -> 0f
            ReviewPerformance.HARD -> 0.7f
            ReviewPerformance.MEDIUM -> 0.8f
            ReviewPerformance.EASY -> 1f
        }
    }
}