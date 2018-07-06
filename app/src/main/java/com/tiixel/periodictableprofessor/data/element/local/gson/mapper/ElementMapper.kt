package com.tiixel.periodictableprofessor.data.element.local.gson.mapper

import com.tiixel.periodictableprofessor.data.element.local.gson.entity.ElementEntity
import com.tiixel.periodictableprofessor.data.element.local.gson.entity.ElementLangEntity
import com.tiixel.periodictableprofessor.datasource.element.generic.StoredElement
import com.tiixel.periodictableprofessor.domain.Quantity
import com.tiixel.periodictableprofessor.util.extensions.nullIfBlank
import javax.measure.unit.NonSI
import javax.measure.unit.NonSI.LITRE
import javax.measure.unit.SI.GRAM
import javax.measure.unit.SI.KILOGRAM
import javax.measure.unit.SI.METRE
import javax.measure.unit.SI.MILLI
import javax.measure.unit.SI.PICO
import javax.measure.unit.Unit

object ElementMapper {

    fun toGeneric(element: ElementEntity, elementLangEntity: ElementLangEntity): StoredElement {

        val abundanceCrust = if (element.abundance_crust.isNotBlank()) element.abundance_crust else null
        val abundanceSea = if (element.abundance_sea.isNotBlank()) element.abundance_sea else null
        val atomicRadius = if (element.atomic_radius.isNotBlank()) element.atomic_radius else null
        val atomicWeight = if (element.atomic_weight.isNotBlank()) element.atomic_weight else null
        val discoveryYear = if (element.discovery_year.isNotBlank()) element.discovery_year else null
        val enPauling = if (element.en_pauling.isNotBlank()) element.en_pauling else null
        val vdwRadius = if (element.vdw_radius.isNotBlank()) element.vdw_radius else null

        return StoredElement(
            abundanceCrust = abundanceCrust?.let { Quantity(it, MILLI(GRAM).divide(KILOGRAM)) },
            abundanceSea = abundanceSea?.let { Quantity(it, MILLI(GRAM).divide(LITRE)) },
            atomicNumber = element.atomic_number,
            atomicRadius = atomicRadius?.let { Quantity(it, PICO(METRE)) },
            atomicWeight = atomicWeight?.let { Quantity(it, NonSI.ATOMIC_MASS) },
            description = element.description.nullIfBlank(),
            discoverers = element.discoverers.nullIfBlank(),
            discoveryLocation = element.discovery_location.nullIfBlank(),
            discoveryYear = discoveryYear?.let { Quantity(it, Unit.ONE) },
            electronicConfiguration = element.electronic_configuration,
            enPauling = enPauling?.let { Quantity(it, Unit.ONE) },
            isRadioactive = element.is_radioactive.toBoolean(),
            name = elementLangEntity.name,
            nameOrigin = elementLangEntity.name_origin.nullIfBlank(),
            sources = element.sources.nullIfBlank(),
            symbol = element.symbol,
            uses = element.uses.nullIfBlank(),
            vdwRadius = vdwRadius?.let { Quantity(it, PICO(METRE)) }
        )
    }
}