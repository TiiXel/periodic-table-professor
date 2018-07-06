package com.tiixel.periodictableprofessor.presentation.review

import com.tiixel.periodictableprofessor.presentation.base.MviAction

sealed class ReviewAction : MviAction {

    data class LoadNext(val newCard: Boolean, val dueSoonOnly: Boolean) : ReviewAction()

    data class Review(val freshReview: com.tiixel.periodictableprofessor.domain.Review.FreshReview) : ReviewAction()

    object GetCounts : ReviewAction()

    object Check : ReviewAction()
}