package com.tiixel.periodictableprofessor.presentation.review

import com.tiixel.periodictableprofessor.domain.ReviewPerformance
import com.tiixel.periodictableprofessor.domain.ReviewableFace
import com.tiixel.periodictableprofessor.presentation.base.MviIntent

sealed class ReviewIntent : MviIntent {

    data class InitialIntent(val newCard: Boolean, val dueSoonOnly: Boolean) : ReviewIntent()

    data class LoadNextCardIntent(val newCard: Boolean, val dueSoonOnly: Boolean) : ReviewIntent()

    data class ReviewCardIntent(val element: Byte, val face: ReviewableFace, val performance: ReviewPerformance) : ReviewIntent()

    object CheckCardIntent : ReviewIntent()
}