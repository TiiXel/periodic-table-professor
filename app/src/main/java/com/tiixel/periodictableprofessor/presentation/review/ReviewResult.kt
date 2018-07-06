package com.tiixel.periodictableprofessor.presentation.review

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.ReviewableFace
import com.tiixel.periodictableprofessor.presentation.base.MviResult

sealed class ReviewResult : MviResult {

    sealed class LoadNextReviewResult : ReviewResult() {

        data class Success(val element: Element, val face: ReviewableFace) : LoadNextReviewResult()

        data class Failure(val error: Throwable) : LoadNextReviewResult()

        object InFlight : LoadNextReviewResult()
    }

    sealed class Review_Result : ReviewResult() {

        object Success : Review_Result()

        data class Failure(val error: Throwable) : Review_Result()

        object InFlight : Review_Result()
    }

    sealed class GetCountsResult : ReviewResult() {

        data class Success(val new: Int, val dueSoon: Int, val dueToday: Int, val nextReviewTimer: List<Int>?) :
            GetCountsResult()

        data class Failure(val error: Throwable) : GetCountsResult()

        object InFlight : GetCountsResult()
    }

    object CheckResult : ReviewResult()
}