package com.tiixel.periodictableprofessor.datarepository.element.mapper

import com.tiixel.periodictableprofessor.datarepository.card.generic.GenericMnemonic
import com.tiixel.periodictableprofessor.datarepository.card.generic.GenericUserNote
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericElement
import com.tiixel.periodictableprofessor.domain.Element

object ElementMapper {

    fun toDomain(element: GenericElement, mnemonic: GenericMnemonic?, userNote: GenericUserNote?): Element {

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