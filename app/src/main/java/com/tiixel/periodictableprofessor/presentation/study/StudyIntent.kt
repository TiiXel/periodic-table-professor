package com.tiixel.periodictableprofessor.presentation.study

import com.tiixel.periodictableprofessor.domain.review.ReviewPerformance
import com.tiixel.periodictableprofessor.domain.review.ReviewableFace
import com.tiixel.periodictableprofessor.presentation.base.MviIntent

sealed class StudyIntent : MviIntent {

    data class InitialIntent(val newCard: Boolean, val dueSoonOnly: Boolean) : StudyIntent()

    data class LoadNextIntent(val newCard: Boolean, val dueSoonOnly: Boolean) : StudyIntent()

    data class ReviewIntent(val element: Byte, val face: ReviewableFace, val performance: ReviewPerformance) : StudyIntent()

    object CheckIntent : StudyIntent()
}