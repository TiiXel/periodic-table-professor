package com.tiixel.periodictableprofessor.datasource.element.mapper

import com.tiixel.periodictableprofessor.datasource.card.generic.StoredMnemonic
import com.tiixel.periodictableprofessor.datasource.card.generic.StoredUserNote
import com.tiixel.periodictableprofessor.datasource.element.generic.StoredElement
import com.tiixel.periodictableprofessor.domain.Element

object ElementMapper {

    fun toDomain(element: StoredElement, mnemonic: StoredMnemonic?, userNote: StoredUserNote?): Element {

        return Element(
            abundanceCrust = element.abundanceCrust,
            abundanceSea = element.abundanceSea,
            atomicNumber = element.atomicNumber,
            atomicRadius = element.atomicRadius,
            atomicWeight = element.atomicWeight,
            description = element.description,
            discoverers = element.discoverers,
            discoveryLocation = element.discoveryLocation,
            discoveryYear = element.discoveryYear,
            electronicConfiguration = element.electronicConfiguration,
            enPauling = element.enPauling,
            isRadioactive = element.isRadioactive,
            name = element.name,
            nameOrigin = element.nameOrigin,
            sources = element.sources,
            symbol = element.symbol,
            uses = element.uses,
            vdwRadius = element.vdwRadius,

            mnemonicPicture = mnemonic?.mnemonicPicture,
            mnemonicPhrase = mnemonic?.mnemonicPhrase,

            mnemonicUserNote = userNote?.userNote
        )
    }
}