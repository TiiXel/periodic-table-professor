package com.tiixel.periodictableprofessor.test

import com.tiixel.periodictableprofessor.domain.review.Review
import com.tiixel.periodictableprofessor.domain.review.ReviewPerformance
import com.tiixel.periodictableprofessor.domain.review.Reviewable
import com.tiixel.periodictableprofessor.domain.review.ReviewableFace
import java.util.Date

object MockReview {

    val now = Date()

    val min = 60 * 1000.toLong()

    val rTimePast = now.time - 2 * 24 * 60 * min
    val rTimeMiss = now.time - 1 * 24 * 60 * min
    val rTimeEarl = now.time - 60 * min
    val rTimeSoon = now.time - 9 * min
    val rTimeLater = now.time - 1 * min

    val dTimePast = rTimePast + 10 * min
    val dTimeEarl = rTimeEarl + 10 * min
    val dTimeSoon = rTimeSoon + 10 * min
    val dTimeLater = rTimeLater + 10 * min

    val e1past = makeMockReview(1, rTimePast + 1, dTimePast + 1)
    val e1pastCram = makeMockReview(1, rTimePast + 1 + 1, dTimePast + 1 + 1)
    val e1earl = makeMockReview(1, rTimeEarl + 1, dTimeEarl + 1)
    val e1soon = makeMockReview(1, rTimeSoon + 1, dTimeSoon + 1)
    val e1later = makeMockReview(1, rTimeLater + 1, dTimeLater + 1)

    val e2past = makeMockReview(2, rTimePast + 2, dTimePast + 2)
    val e2earl = makeMockReview(2, rTimeEarl + 2, dTimeEarl + 2)
    val e2soon = makeMockReview(2, rTimeSoon + 2, dTimeSoon + 2)
    val e2later = makeMockReview(2, rTimeLater + 2, dTimeLater + 2)

    val e3past = makeMockReview(3, rTimePast + 3, dTimePast + 3)
    val e3earl = makeMockReview(3, rTimeEarl + 3, dTimeEarl + 3)
    val e3soon = makeMockReview(3, rTimeSoon + 3, dTimeSoon + 3)
    val e3later = makeMockReview(3, rTimeLater + 3, dTimeLater + 3)

    fun makeMockReview(id: Byte, reviewDate: Long, nextDueDate: Long): Review {
        return Review(
            item = Reviewable(id),
            face = ReviewableFace.PICTURE,
            reviewDate = Date(reviewDate),
            nextDueDate = Date(nextDueDate),
            performance = ReviewPerformance.FAILED,
            aggregatedItemDifficulty = 0.3f
        )
    }
}