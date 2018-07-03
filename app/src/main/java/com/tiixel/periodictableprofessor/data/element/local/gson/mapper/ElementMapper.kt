package com.tiixel.periodictableprofessor.data.element.local.gson.mapper

import com.tiixel.periodictableprofessor.data.element.local.gson.entity.ElementEntity
import com.tiixel.periodictableprofessor.data.element.local.gson.entity.ElementLangEntity
import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.Quantity
import javax.measure.unit.NonSI
import javax.measure.unit.NonSI.LITRE
import javax.measure.unit.SI.GRAM
import javax.measure.unit.SI.KILOGRAM
import javax.measure.unit.SI.METRE
import javax.measure.unit.SI.MILLI
import javax.measure.unit.SI.PICO

object ElementMapper {

    fun toDomain(element: ElementEntity, elementLangEntity: ElementLangEntity): Element {

        val abundanceCrust = if (element.abundance_crust.isNotBlank()) element.abundance_crust else null
        val abundanceSea = if (element.abundance_sea.isNotBlank()) element.abundance_sea else null
        val atomicRadius = if (element.atomic_radius.isNotBlank()) element.atomic_radius else null
        val atomicWeight = if (element.atomic_weight.isNotBlank()) element.atomic_weight else null
        val vdwRadius = if (element.vdw_radius.isNotBlank()) element.vdw_radius else null

        return Element(
            abundanceCrust = abundanceCrust?.let { Quantity(it, MILLI(GRAM).divide(KILOGRAM)) },
            abundanceSea = abundanceSea?.let { Quantity(it, MILLI(GRAM).divide(LITRE)) },
            atomicNumber = element.atomic_number,
            atomicRadius = atomicRadius?.let { Quantity(it, PICO(METRE)) },
            atomicWeight = atomicWeight?.let { Quantity(it, NonSI.ATOMIC_MASS) },
            description = element.description,
            discoverers = element.discoverers,
            discoveryLocation = element.discovery_location,
            discoveryYear = element.discovery_year.toIntOrNull(),
            electronicConfiguration = element.electronic_configuration,
            enPauling = element.en_pauling.toFloatOrNull(),
            isRadioactive = element.is_radioactive.toBoolean(),
            name = elementLangEntity.name,
            nameOrigin = elementLangEntity.name_origin,
            sources = element.sources,
            symbol = element.symbol,
            uses = element.uses,
            vdwRadius = vdwRadius?.let { Quantity(it, PICO(METRE)) }
        )
    }
}