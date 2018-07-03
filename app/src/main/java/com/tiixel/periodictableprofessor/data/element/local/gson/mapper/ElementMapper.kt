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

        return Element(
            abundanceCrust = Quantity(element.abundance_crust, MILLI(GRAM).divide(KILOGRAM)),
            abundanceSea = Quantity(element.abundance_sea, MILLI(GRAM).divide(LITRE)),
            atomicNumber = element.atomic_number,
            atomicRadius = Quantity(element.atomic_radius, PICO(METRE)),
            atomicWeight = Quantity(element.atomic_weight, NonSI.ATOMIC_MASS),
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
            vdwRadius = Quantity(element.vdw_radius, PICO(METRE))
        )
    }
}