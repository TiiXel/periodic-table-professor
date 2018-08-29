package com.tiixel.periodictableprofessor.datarepository.element.mapper

import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericElement
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericMnemonic
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericUserNote
import com.tiixel.periodictableprofessor.domain.element.Element

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

    fun toGenericElement(element: Element): GenericElement {
        return GenericElement(
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
            vdwRadius = element.vdwRadius
        )
    }

    fun toGenericMnemonic(element: Element): GenericMnemonic {
        return GenericMnemonic(
            element = element.atomicNumber,
            mnemonicPhrase = element.mnemonicPhrase,
            mnemonicPicture = element.mnemonicPicture
        )
    }

    fun toGenericUserNote(element: Element): GenericUserNote {
        return GenericUserNote(
            element = element.atomicNumber,
            userNote = element.mnemonicUserNote
        )
    }
}