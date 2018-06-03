package com.tiixel.periodictableprofessor.data.element.local.sqlite.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tiixel.periodictableprofessor.data.element.local.sqlite.ELEMENTS_TABLE_NAME
import com.tiixel.periodictableprofessor.domain.Element

@Entity(tableName = ELEMENTS_TABLE_NAME)
data class ElementEntity(
    val abundance_crust: Float?,
    val abundance_sea: Float?,
    @PrimaryKey
    val atomic_number: Byte,
    val atomic_radius: Float?,
    val atomic_weight: Float?,
    val description: String?,
    val discoverers: String?,
    val discovery_location: String?,
    val discovery_year: Int?,
    val electronic_configuration: String,
    val en_pauling: Float?,
    val is_radioactive: Boolean,
    val sources: String?,
    val symbol: String,
    val uses: String?,
    val vdw_radius: Float?,

    val name: String,
    val name_origin: String
) {

    companion object {

        fun toDomain(entity: ElementEntity): Element {

            return Element(
                abundanceCrust = entity.abundance_crust,
                abundanceSea = entity.abundance_sea,
                atomicNumber = entity.atomic_number.toByte(),
                atomicRadius = entity.atomic_radius,
                atomicWeight = entity.atomic_weight,
                description = entity.description,
                discoverers = entity.discoverers,
                discoveryLocation = entity.discovery_location,
                discoveryYear = entity.discovery_year,
                electronicConfiguration = entity.electronic_configuration,
                enPauling = entity.en_pauling,
                isRadioactive = entity.is_radioactive,
                name = entity.name,
                nameOrigin = entity.name_origin,
                sources = entity.sources,
                symbol = entity.symbol,
                uses = entity.uses,
                vdwRadius = entity.vdw_radius
            )
        }
    }
}