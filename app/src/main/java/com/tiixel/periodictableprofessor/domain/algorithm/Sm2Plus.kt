package com.tiixel.periodictableprofessor.domain.algorithm

import com.tiixel.periodictableprofessor.domain.review.Review
import com.tiixel.periodictableprofessor.domain.review.ReviewPerformance
import com.tiixel.periodictableprofessor.domain.review.Reviewable
import java.util.Date
import kotlin.math.max
import kotlin.math.min

object Sm2Plus {

    fun defaultReview(freshReview: Review.FreshReview) =
        Review(
            item = Reviewable(freshReview.item.itemId),
            face = freshReview.face,
            reviewDate = freshReview.reviewDate,
            nextDueDate = Date(freshReview.reviewDate.time + 10 * 60 * 1000),
            performance = freshReview.performance,
            aggregatedItemDifficulty = 0.3f
        )

    /**
     * http://www.blueraja.com/blog/477/a-better-spaced-repetition-learning-algorithm-sm2
     */
    fun aggregator(lastReview: Review, freshReview: Review.FreshReview): Review {

        if (lastReview.item.itemId != freshReview.item.itemId) throw IllegalArgumentException("You cannot aggregate reviews for different items.")

        val corrOverdue = if (freshReview.performance == ReviewPerformance.FAILED) 1f else min(
            2f,
            lastReview.nextOverdue(freshReview.reviewDate)
        )

        val newDifficulty =
            min(
                max(
                    lastReview.aggregatedItemDifficulty + corrOverdue * 1 / 17f * (8 - 9f * performanceRating(
                        freshReview.performance
                    )),
                    0f
                ),
                1f
            )

        val difficultyWeight = 3 - 1.7f * newDifficulty

        val intervalCorrector =
            if (freshReview.performance == ReviewPerformance.FAILED) 1 / difficultyWeight / difficultyWeight
            else 1 + (difficultyWeight - 1) * corrOverdue

        val nextDueDate = Date(freshReview.reviewDate.time + (lastReview.nextInterval() * intervalCorrector).toLong())

        return Review(
            item = freshReview.item,
            face = freshReview.face,
            reviewDate = freshReview.reviewDate,
            nextDueDate = nextDueDate,
            performance = freshReview.performance,
            aggregatedItemDifficulty = newDifficulty
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