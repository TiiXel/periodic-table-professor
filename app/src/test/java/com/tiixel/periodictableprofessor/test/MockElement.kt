package com.tiixel.periodictableprofessor.test

import com.tiixel.periodictableprofessor.domain.element.Element
import com.tiixel.periodictableprofessor.domain.element.Quantity
import javax.measure.unit.Unit

object MockElement {

    private val one = Quantity("1", Unit.ONE)

    val e1 = makeMockElement(1)
    val e2 = makeMockElement(2)
    val e3 = makeMockElement(3)

    fun makeMockElement(atomicNumber: Byte): Element {
        return Element(
            atomicNumber = atomicNumber,
            abundanceCrust = one,
            abundanceSea = one,
            atomicRadius = one,
            atomicWeight = one,
            description = "",
            discoverers = "",
            discoveryLocation = "",
            discoveryYear = one,
            electronicConfiguration = "",
            enPauling = one,
            isRadioactive = false,
            name = "Mockium$atomicNumber",
            nameOrigin = "",
            sources = "",
            symbol = "Mk",
            uses = "",
            vdwRadius = one,
            mnemonicPhrase = "",
            mnemonicPicture = ByteArray(2),
            mnemonicUserNote = ""
        )
    }

}