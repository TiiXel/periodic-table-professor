package com.tiixel.periodictableprofessor.data.review.local.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tiixel.periodictableprofessor.data.review.local.db.REVIEW_LOG_TABLE_NAME
import com.tiixel.periodictableprofessor.datarepository.card.generic.GenericReview
import java.util.Date

@Entity(tableName = REVIEW_LOG_TABLE_NAME)
data class ReviewEntity(
    val itemId: Byte,
    val face: Int?,
    @PrimaryKey
    val review_date: Long,
    val next_due_date: Long,
    val performance: Int,
    val aggregated_item_difficulty: Float
) {

    companion object {

        fun toGeneric(entity: ReviewEntity): GenericReview {
            return GenericReview(
                itemId = entity.itemId,
                face = entity.face ?: 0,
                reviewDate = Date(entity.review_date),
                nextDueDate = Date(entity.next_due_date),
                performance = entity.performance,
                aggregatedItemDifficulty = entity.aggregated_item_difficulty
            )
        }

        fun fromGeneric(generic: GenericReview): ReviewEntity {
            return ReviewEntity(
                itemId = generic.itemId,
                face = generic.face,
                review_date = generic.reviewDate.time,
                next_due_date = generic.nextDueDate.time,
                performance = generic.performance,
                aggregated_item_difficulty = generic.aggregatedItemDifficulty
            )
        }
    }
}