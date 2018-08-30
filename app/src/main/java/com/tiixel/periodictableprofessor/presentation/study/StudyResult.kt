package com.tiixel.periodictableprofessor.presentation.study

import com.tiixel.periodictableprofessor.domain.element.Element
import com.tiixel.periodictableprofessor.domain.review.ReviewableFace
import com.tiixel.periodictableprofessor.presentation.base.MviResult

sealed class StudyResult : MviResult {

    sealed class LoadNextReviewResult : StudyResult() {

        data class Success(val element: Element, val face: ReviewableFace) : LoadNextReviewResult()

        data class Failure(val error: Throwable) : LoadNextReviewResult()

        object InFlight : LoadNextReviewResult()
    }

    sealed class ReviewResult : StudyResult() {

        object Success : ReviewResult()

        data class Failure(val error: Throwable) : ReviewResult()

        object InFlight : ReviewResult()
    }

    sealed class GetCountsResult : StudyResult() {

        data class Success(val new: Int, val dueSoon: Int, val dueToday: Int, val nextReviewTimer: List<Int>?) :
            GetCountsResult()

        data class Failure(val error: Throwable) : GetCountsResult()

        object InFlight : GetCountsResult()
    }

    object CheckResult : StudyResult()
}