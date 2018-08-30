package com.tiixel.periodictableprofessor.datarepository.element.mapper

import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericElement
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericMnemonic
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericUserNote
import com.tiixel.periodictableprofessor.domain.element.Element
import com.tiixel.periodictableprofessor.test.MockElement.e1
import org.junit.Assume
import org.junit.Test

class ElementMapperTest {

    // toDomain()
    @Test
    fun toDomain_maps() {
        val result = util_toDomain_maps()

        assert(result)
    }

    // toGenericElement() & toGenericMnemonic() & toGenericUserNote()
    @Test
    fun toGeneric_maps() {
        Assume.assumeTrue(util_toGeneric_maps())

        val result = util_toGeneric_maps()

        assert(result)
    }

    companion object {

        fun util_toGeneric_maps(): Boolean {
            val element = ElementMapper.toGenericElement(e1)
            val mnemonic = ElementMapper.toGenericMnemonic(e1)
            val userNote = ElementMapper.toGenericUserNote(e1)

            return domain_genericElement_are_equals(e1, element)
                && domain_genericMnemonic_are_equals(e1, mnemonic)
                && domain_genericUserNote_are_equals(e1, userNote)
        }

        fun util_toDomain_maps(): Boolean {
            val element = ElementMapper.toGenericElement(e1)
            val mnemonic = ElementMapper.toGenericMnemonic(e1)
            val userNote = ElementMapper.toGenericUserNote(e1)

            val mapped = ElementMapper.toDomain(element, mnemonic, userNote)

            return domain_genericElement_are_equals(mapped, element)
                && domain_genericMnemonic_are_equals(mapped, mnemonic)
                && domain_genericUserNote_are_equals(mapped, userNote)
        }

        fun domain_genericElement_are_equals(domain: Element, generic: GenericElement): Boolean {
            if (generic.abundanceCrust == domain.abundanceCrust
                && generic.abundanceSea == domain.abundanceSea
                && generic.atomicNumber == domain.atomicNumber
                && generic.atomicRadius == domain.atomicRadius
                && generic.atomicWeight == domain.atomicWeight
                && generic.description == domain.description
                && generic.discoverers == domain.discoverers
                && generic.discoveryLocation == domain.discoveryLocation
                && generic.discoveryYear == domain.discoveryYear
                && generic.electronicConfiguration == domain.electronicConfiguration
                && generic.enPauling == domain.enPauling
                && generic.isRadioactive == domain.isRadioactive
                && generic.name == domain.name
                && generic.nameOrigin == domain.nameOrigin
                && generic.sources == domain.sources
                && generic.symbol == domain.symbol
                && generic.uses == domain.uses
                && generic.vdwRadius == domain.vdwRadius
            ) return true
            return false
        }

        fun domain_genericMnemonic_are_equals(domain: Element, generic: GenericMnemonic): Boolean {
            if (generic.element == domain.atomicNumber
                && generic.mnemonicPhrase == domain.mnemonicPhrase
                && generic.mnemonicPicture?.hashCode() == domain.mnemonicPicture?.hashCode()
            ) return true
            return false
        }

        fun domain_genericUserNote_are_equals(domain: Element, generic: GenericUserNote): Boolean {
            if (generic.element == domain.atomicNumber
                && generic.userNote == domain.mnemonicUserNote
            ) return true
            return false
        }
    }
}