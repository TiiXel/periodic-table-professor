package com.tiixel.periodictableprofessor.presentation.study.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tiixel.periodictableprofessor.domain.element.Element
import com.tiixel.periodictableprofessor.domain.review.ReviewableFace

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

        fun mapFromDomain(element: Element, face: ReviewableFace, revealed: Boolean = false): CardModel {
            return CardModel(
                number = element.atomicNumber.toString(),
                symbol = element.symbol,
                name = element.name,
                mnemonicPicture = element.mnemonicPicture?.let {
                        BitmapFactory.decodeByteArray(
                            it,
                            0,
                            it.size
                        )
                    },
                mnemonicPhrase = element.mnemonicPhrase ?: "",
                column = element.tableColumn,
                row = element.tableRow,
                userNote = element.mnemonicUserNote ?: "",
                visibleNumber = revealed || face == ReviewableFace.NUMBER,
                visibleSymbol = revealed || face == ReviewableFace.SYMBOL,
                visibleMnemonicPhrase = revealed,
                visibleMnemonicPicture = revealed || face == ReviewableFace.PICTURE,
                visibleName = revealed || face == ReviewableFace.NAME,
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