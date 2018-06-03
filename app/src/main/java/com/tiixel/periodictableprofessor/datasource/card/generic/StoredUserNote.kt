package com.tiixel.periodictableprofessor.datasource.card.generic

import com.tiixel.periodictableprofessor.domain.Card

data class StoredUserNote(
    val element: Byte,
    val userNote: String?
) {
    companion object {

        fun toDomain(generic: StoredUserNote): Card.UserNote? {
            if (generic.userNote == null) {
                return null
            }
            return Card.UserNote(
                    userNote = generic.userNote
                )
        }
    }
}