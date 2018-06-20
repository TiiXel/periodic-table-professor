package com.tiixel.periodictableprofessor.factory

import com.tiixel.periodictableprofessor.domain.Card
import com.tiixel.periodictableprofessor.factory.DataFactory.Factory.randomUuid

object CardFactory {

    fun makeCard(atomicNumber: Byte? = null) =
        Card(
            element = ElementFactory.makeElement(atomicNumber),
            mnemonic = makeMnemonic(),
            userNote = makeUserNote()
        )

    fun makeMnemonic() =
        Card.Mnemonic(
            phrase = randomUuid(),
            picture = ByteArray(2)
        )

    fun makeUserNote() =
        Card.UserNote(
            userNote = randomUuid()
        )
}