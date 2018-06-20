package com.tiixel.periodictableprofessor.factory

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.factory.DataFactory.Factory.makeAtomicNumber
import com.tiixel.periodictableprofessor.factory.DataFactory.Factory.randomBoolean
import com.tiixel.periodictableprofessor.factory.DataFactory.Factory.randomFloat
import com.tiixel.periodictableprofessor.factory.DataFactory.Factory.randomInt
import com.tiixel.periodictableprofessor.factory.DataFactory.Factory.randomUuid

object ElementFactory {

    fun makeElement(atomicNumber: Byte? = null) =
        Element(
            abundanceCrust = randomFloat(),
            abundanceSea = randomFloat(),
            atomicNumber = atomicNumber ?: makeAtomicNumber(),
            atomicRadius = randomFloat(),
            atomicWeight = randomFloat(),
            description = randomUuid(),
            discoverers = randomUuid(),
            discoveryLocation = randomUuid(),
            discoveryYear = randomInt(),
            electronicConfiguration = randomUuid(),
            enPauling = randomFloat(),
            isRadioactive = randomBoolean(),
            name = randomUuid(),
            nameOrigin = randomUuid(),
            sources = randomUuid(),
            symbol = randomUuid().substring(0, 2),
            uses = randomUuid(),
            vdwRadius = randomFloat()
        )

    fun makeElements(): List<Element> {
        return MutableList(118, { makeElement((it + 1).toByte()) })
    }
}