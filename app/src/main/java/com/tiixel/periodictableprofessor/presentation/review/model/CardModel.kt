package com.tiixel.periodictableprofessor.presentation.review.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tiixel.periodictableprofessor.domain.Card

data class CardModel(
    val number: String,
    val symbol: String,
    val name: String,
    val mnemonicPicture: Bitmap?,
    val mnemonicPhrase: String,
    val column: Byte,
    val row: Byte,
    val userNote: String,
    val visibleNumber: Boolean,
    val visibleSymbol: Boolean,
    val visibleName: Boolean,
    val visibleMnemonicPhrase: Boolean,
    val visibleMnemonicPicture: Boolean,
    val visibleUserNote: Boolean
) {

    companion object {

        fun mapFromDomain(card: Card, face: Card.Companion.Face, revealed: Boolean = false): CardModel {
            return CardModel(
                number = card.element.atomicNumber.toString(),
                symbol = card.element.symbol,
                name = card.element.name,
                mnemonicPicture = card.mnemonic?.let {
                    it.picture.let {
                        BitmapFactory.decodeByteArray(
                            it,
                            0,
                            it.size
                        )
                    }
                },
                mnemonicPhrase = card.mnemonic?.phrase ?: "",
                column = card.element.tableColumn,
                row = card.element.tableRow,
                userNote = card.userNote?.userNote ?: "",
                visibleNumber = revealed || face == Card.Companion.Face.NUMBER,
                visibleSymbol = revealed || face == Card.Companion.Face.SYMBOL,
                visibleMnemonicPhrase = revealed,
                visibleMnemonicPicture = revealed || face == Card.Companion.Face.PICTURE,
                visibleName = revealed || face == Card.Companion.Face.NAME,
                visibleUserNote = revealed
            )
        }

        fun reveal(card: CardModel): CardModel {
            return card.copy(
                visibleNumber = true,
                visibleSymbol = true,
                visibleMnemonicPicture = true,
                visibleName = true,
                visibleMnemonicPhrase = true,
                visibleUserNote = true
            )
        }
    }
}