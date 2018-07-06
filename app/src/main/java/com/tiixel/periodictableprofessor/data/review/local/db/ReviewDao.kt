package com.tiixel.periodictableprofessor.data.review.local.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.tiixel.periodictableprofessor.data.review.local.db.entity.ReviewEntity

const val REVIEW_LOG_TABLE_NAME = "review_log"

@Dao
abstract class ReviewDao {

    @Query(value = "SELECT * FROM review_log")
    abstract fun getReviewLog(): List<ReviewEntity>

    @Insert
    abstract fun logReview(reviewEntity: ReviewEntity)
}