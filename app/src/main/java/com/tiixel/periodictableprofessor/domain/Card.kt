package com.tiixel.periodictableprofessor.domain

data class Card(
    val element: Element,
    val mnemonic: Mnemonic?,
    val userNote: UserNote?
) {

    companion object {

        enum class Face {
            PICTURE,
            NUMBER,
            SYMBOL,
            NAME
        }

        enum class Performance {
            FAILED,
            HARD,
            MEDIUM,
            EASY
        }
    }

    data class Mnemonic(
        val phrase: String,
        val picture: ByteArray
    )

    data class UserNote(
        val userNote: String
    )
}