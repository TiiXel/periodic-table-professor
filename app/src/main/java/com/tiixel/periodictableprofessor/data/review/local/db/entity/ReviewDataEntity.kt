package com.tiixel.periodictableprofessor.data.review.local.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tiixel.periodictableprofessor.data.review.local.db.REVIEW_LOG_TABLE_NAME
import com.tiixel.periodictableprofessor.datarepository.card.generic.StoredReviewData
import java.util.Date

@Entity(tableName = REVIEW_LOG_TABLE_NAME)
data class ReviewDataEntity(
    val element: Byte,
    val difficulty: Float,
    @PrimaryKey
    val review_date: Long,
    val next_interval: Long,
    val performance: Int?
) {

    companion object {

        fun toGeneric(entity: ReviewDataEntity): StoredReviewData {
            return StoredReviewData(
                element = entity.element,
                difficulty = entity.difficulty,
                reviewDate = entity.review_date?.let { Date(it) },
                nextInterval = entity.next_interval,
                performance = entity.performance
            )
        }

        fun fromGeneric(generic: StoredReviewData): ReviewDataEntity {
            return ReviewDataEntity(
                element = generic.element,
                difficulty = generic.difficulty,
                review_date = generic.reviewDate?.time,
                next_interval = generic.nextInterval,
                performance = generic.performance
            )
        }
    }
}