package com.tiixel.periodictableprofessor.datarepository.card.generic

import com.tiixel.periodictableprofessor.domain.ReviewData
import com.tiixel.periodictableprofessor.domain.ReviewPerformance
import java.util.Date

data class StoredReviewData(
    val element: Byte,
    val difficulty: Float,
    val reviewDate: Date,
    val nextInterval: Long,
    val performance: Int?
) {

    companion object {

        fun fromDomain(reviewData: ReviewData): StoredReviewData {
            return StoredReviewData(
                element = reviewData.element,
                nextInterval = reviewData.nextInterval,
                reviewDate = reviewData.reviewDate,
                difficulty = reviewData.difficulty,
                performance = reviewData.performance?.ordinal
            )
        }

        fun toDomain(generic: StoredReviewData): ReviewData {
            return ReviewData(
                element = generic.element,
                nextInterval = generic.nextInterval,
                reviewDate = generic.reviewDate,
                _difficulty = generic.difficulty,
                performance = generic.performance?.let { ReviewPerformance.values()[it] }
            )
        }
    }
}