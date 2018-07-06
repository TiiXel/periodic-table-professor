package com.tiixel.periodictableprofessor.datarepository.card.mapper

import com.tiixel.periodictableprofessor.datarepository.card.generic.GenericReview
import com.tiixel.periodictableprofessor.domain.Review
import com.tiixel.periodictableprofessor.domain.ReviewPerformance
import com.tiixel.periodictableprofessor.domain.Reviewable
import com.tiixel.periodictableprofessor.domain.ReviewableFace

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