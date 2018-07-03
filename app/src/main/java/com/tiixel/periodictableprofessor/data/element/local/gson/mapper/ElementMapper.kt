package com.tiixel.periodictableprofessor.data.element.local.gson.mapper

import com.tiixel.periodictableprofessor.data.element.local.gson.entity.ElementEntity
import com.tiixel.periodictableprofessor.data.element.local.gson.entity.ElementLangEntity
import com.tiixel.periodictableprofessor.domain.Element

object ElementMapper {

    fun toDomain(element: ElementEntity, elementLangEntity: ElementLangEntity): Element {

        return Element(
            abundanceCrust = element.abundance_crust.toFloatOrNull(),
            abundanceSea = element.abundance_sea.toFloatOrNull(),
            atomicNumber = element.atomic_number.toByte(),
            atomicRadius = element.atomic_radius.toFloatOrNull(),
            atomicWeight = element.atomic_weight.toFloatOrNull(),
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
            vdwRadius = element.vdw_radius.toFloatOrNull()
        )
    }
}