package com.tiixel.periodictableprofessor.datarepository.review.mapper

import com.tiixel.periodictableprofessor.datarepository.review.generic.GenericReview
import com.tiixel.periodictableprofessor.domain.review.Review
import com.tiixel.periodictableprofessor.domain.review.ReviewPerformance
import com.tiixel.periodictableprofessor.domain.review.Reviewable
import com.tiixel.periodictableprofessor.domain.review.ReviewableFace

object ReviewMapper {

    fun toGeneric(review: Review): GenericReview {
        return GenericReview(
            itemId = review.item.itemId,
            face = review.face.ordinal,
            reviewDate = review.reviewDate,
            performance = review.performance.ordinal,
            nextDueDate = review.nextDueDate,
            aggregatedItemDifficulty = review.aggregatedItemDifficulty
        )
    }

    fun toDomain(generic: GenericReview): Review {
        return Review(
            item = Reviewable(generic.itemId),
            face = ReviewableFace.values()[generic.face],
            reviewDate = generic.reviewDate,
            performance = ReviewPerformance.values()[generic.performance],
            nextDueDate = generic.nextDueDate,
            aggregatedItemDifficulty = generic.aggregatedItemDifficulty
        )
    }
}