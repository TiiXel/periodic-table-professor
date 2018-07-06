package com.tiixel.periodictableprofessor.datarepository.card.generic

import java.util.Date

data class GenericReview(
    val date: Date,
    val element: Byte,
    val performance: Byte
) {

    companion object {

    }
}