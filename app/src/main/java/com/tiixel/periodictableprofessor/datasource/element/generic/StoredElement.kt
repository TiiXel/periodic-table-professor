package com.tiixel.periodictableprofessor.datasource.element.generic

import com.tiixel.periodictableprofessor.domain.Quantity

data class StoredElement(
    val abundanceCrust: Quantity?,
    val abundanceSea: Quantity?,
    val atomicNumber: Byte,
    val atomicRadius: Quantity?,
    val atomicWeight: Quantity?,
    val description: String?,
    val discoverers: String?,
    val discoveryLocation: String?,
    val discoveryYear: Quantity?,
    val electronicConfiguration: String,
    val enPauling: Quantity?,
    val isRadioactive: Boolean,
    val name: String,
    val nameOrigin: String?,
    val sources: String?,
    val symbol: String,
    val uses: String?,
    val vdwRadius: Quantity?
)