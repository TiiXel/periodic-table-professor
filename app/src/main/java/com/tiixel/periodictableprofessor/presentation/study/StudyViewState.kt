package com.tiixel.periodictableprofessor.presentation.study

import com.tiixel.periodictableprofessor.presentation.base.MviViewState
import com.tiixel.periodictableprofessor.presentation.study.model.ElementModel

data class StudyViewState(
    val itemId: Byte?,
    val element: ElementModel?,
    val isNumberVisible: Boolean,
    val isSymbolVisible: Boolean,
    val isNameVisible: Boolean,
    val isTablePositionVisible: Boolean,
    val isPictureVisible: Boolean,
    val isPhraseVisible: Boolean,
    val isUserNoteVisible: Boolean,
    val newCardCount: Int,
    val dueSoonCount: Int,
    val dueTodayCount: Int,
    val nextReviewTimer: List<Int>?,
    val loadingInProgress: Boolean,
    val loadingFailedCause: Exception?,
    val reviewingInProgress: Boolean,
    val reviewingFailed: Boolean,
    val showCheckButtonOverPerformance: Boolean
) : MviViewState {

    companion object {

        fun init(): StudyViewState {
            return StudyViewState(
                itemId = null,
                element = null,
                isNumberVisible = false,
                isSymbolVisible = false,
                isNameVisible = false,
                isTablePositionVisible = false,
                isPictureVisible = false,
                isPhraseVisible = false,
                isUserNoteVisible = false,
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