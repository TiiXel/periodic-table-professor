package com.tiixel.periodictableprofessor.presentation.review.mapper

import android.graphics.BitmapFactory
import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.presentation.review.model.ElementModel

object ElementMapper {

    fun fromDomain(element: Element): ElementModel {
        return ElementModel(
            number = element.atomicNumber.toString(),
            symbol = element.symbol,
            name = element.name,
            column = element.tableColumn,
            row = element.tableRow,
            mnemonicPicture = element.mnemonicPicture?.let {
                BitmapFactory.decodeByteArray(
                    it,
                    0,
                    it.size
                )
            },
            mnemonicPhrase = element.mnemonicPhrase,
            userNote = element.mnemonicUserNote
        )
    }
}