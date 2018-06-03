package com.tiixel.periodictableprofessor.presentation.elementlist.model

import com.tiixel.periodictableprofessor.domain.Element

data class ElementModel(
    val atomicNumber: Byte,
    val symbol: String,
    val column: Byte,
    val row: Byte,
    val data: String?
) {

    companion object {

        fun listFromDomain(elements: List<Element>, dataPoint: OneLineDataPoint?): List<ElementModel> {
            return elements.map {
                ElementModel(
                    atomicNumber = it.atomicNumber,
                    symbol = it.symbol,
                    column = it.tableColumn,
                    row = it.tableRow,
                    data = if (dataPoint != null) getData(it, dataPoint) else null
                )
            }.sortedBy { it.data?.toFloatOrNull() ?: Float.MIN_VALUE }
        }

        private fun getData(element: Element, dataPoint: OneLineDataPoint): String {
            return when (dataPoint) {
                OneLineDataPoint.ABUNDANCE_CRUST -> element.abundanceCrust?.toString() ?: ""
                OneLineDataPoint.ABUNDANCE_SEA -> element.abundanceSea?.toString() ?: ""
                OneLineDataPoint.ATOMIC_RADIUS -> element.atomicRadius?.toString() ?: ""
                OneLineDataPoint.ATOMIC_WEIGHT -> element.atomicWeight?.toString() ?: ""
                OneLineDataPoint.DISCOVERY_LOCATION -> element.discoveryLocation ?: ""
                OneLineDataPoint.DISCOVERY_YEAR -> element.discoveryYear?.toString() ?: ""
                OneLineDataPoint.ELECTRONIC_CONFIGURATION -> element.electronicConfiguration
                OneLineDataPoint.EN_PAULING -> element.enPauling?.toString() ?: ""
                OneLineDataPoint.NAME -> element.name ?: ""
                OneLineDataPoint.VDW_RADIUS -> element.vdwRadius?.toString() ?: ""
            }
        }

        enum class OneLineDataPoint {
            ABUNDANCE_CRUST,
            ABUNDANCE_SEA,
            ATOMIC_RADIUS,
            ATOMIC_WEIGHT,
            DISCOVERY_LOCATION,
            DISCOVERY_YEAR,
            ELECTRONIC_CONFIGURATION,
            EN_PAULING,
            NAME,
            VDW_RADIUS
        }
    }
}