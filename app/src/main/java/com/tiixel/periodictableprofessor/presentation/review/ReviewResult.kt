package com.tiixel.periodictableprofessor.presentation.review

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.ReviewableFace
import com.tiixel.periodictableprofessor.presentation.base.MviResult

sealed class ReviewResult : MviResult {

    sealed class LoadNextCardResult : ReviewResult() {

        data class Success(val element: Element, val face: ReviewableFace) : LoadNextCardResult()

        data class Failure(val error: Throwable) : LoadNextCardResult()

        object InFlight : LoadNextCardResult()
    }

    sealed class ReviewCardResult : ReviewResult() {

        object Success : ReviewCardResult()

        data class Failure(val error: Throwable) : ReviewCardResult()

        object InFlight : ReviewCardResult()
    }

    sealed class GetCountsResult : ReviewResult() {

        data class Success(val new: Int, val dueSoon: Int, val dueToday: Int, val nextReviewTimer: List<Int>?) :
            GetCountsResult()

        data class Failure(val error: Throwable) : GetCountsResult()

        object InFlight : GetCountsResult()
    }

    object CheckCardResult : ReviewResult()
}