package com.tiixel.periodictableprofessor.presentation.review

import com.tiixel.periodictableprofessor.domain.ReviewPerformance
import com.tiixel.periodictableprofessor.presentation.base.MviAction

sealed class ReviewAction : MviAction {

    data class LoadNextCard(val newCard: Boolean, val dueSoonOnly: Boolean) : ReviewAction()

    data class ReviewCard(val element: Byte, val performance: ReviewPerformance) : ReviewAction()

    object GetCounts : ReviewAction()

    object CheckCard : ReviewAction()
}