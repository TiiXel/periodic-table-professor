package com.tiixel.periodictableprofessor.presentation.review

import com.tiixel.periodictableprofessor.presentation.base.MviViewState
import com.tiixel.periodictableprofessor.presentation.review.model.CardModel

data class ReviewViewState(
    val card: CardModel?,
    val newCardCount: Int,
    val dueSoonCount: Int,
    val dueTodayCount: Int,
    val nextReviewTimer: List<Int>?,
    val loadingInProgress: Boolean,
    /**
     * Can take error values from:
     *  - [ReviewResult.LoadNextCardResult.Failure]
     */
    val loadingFailedCause: Exception?,
    val reviewingInProgress: Boolean,
    val reviewingFailed: Boolean,
    val showCheckButtonOverPerformance: Boolean
) : MviViewState {

    companion object {

        fun init(): ReviewViewState {
            return ReviewViewState(
                card = null,
                newCardCount = 0,
                dueSoonCount = 0,
                dueTodayCount = 0,
                loadingInProgress = true,
                loadingFailedCause = null,
                reviewingInProgress = false,
                reviewingFailed = false,
                showCheckButtonOverPerformance = true,
                nextReviewTimer = null
            )
        }
    }
}