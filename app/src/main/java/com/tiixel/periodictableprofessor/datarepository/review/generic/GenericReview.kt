package com.tiixel.periodictableprofessor.datarepository.review.generic

import java.util.Date

data class GenericReview(
    val itemId: Byte,
    val face: Int,
    val reviewDate: Date,
    val performance: Int,
    val nextDueDate: Date,

    val aggregatedItemDifficulty: Float
)