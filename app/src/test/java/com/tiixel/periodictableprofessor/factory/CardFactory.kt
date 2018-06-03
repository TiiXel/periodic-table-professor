package com.tiixel.periodictableprofessor.factory

import com.tiixel.periodictableprofessor.domain.Card
import io.github.benas.randombeans.EnhancedRandomBuilder

object CardFactory {

    fun makeCard(atomicNumber: Byte) =
        EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .build()
            .nextObject(Card::class.java)
            .copy(element = ElementFactory.makeElement(atomicNumber))
}