package com.tiixel.periodictableprofessor.presentation.study

import com.tiixel.periodictableprofessor.presentation.base.MviAction

sealed class StudyAction : MviAction {

    data class LoadNext(val newCard: Boolean, val dueSoonOnly: Boolean) : StudyAction()

    data class Review(val freshReview: com.tiixel.periodictableprofessor.domain.review.Review.FreshReview) : StudyAction()

    object GetCounts : StudyAction()

    object Check : StudyAction()
}