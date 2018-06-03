package com.tiixel.periodictableprofessor.factory

import com.tiixel.periodictableprofessor.domain.Element
import io.github.benas.randombeans.EnhancedRandomBuilder

object ElementFactory {

    fun makeElement(atomicNumber: Byte) =
        EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .build()
            .nextObject(Element::class.java)
            .copy(atomicNumber = atomicNumber)

    fun makeElements(): List<Element> {
        return MutableList(118, { makeElement((it + 1).toByte()) })
    }
}