package com.tiixel.periodictableprofessor.presentation.elementlist.model

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.Quantity

data class ElementModel(
    val atomicNumber: Byte,
    val symbol: String,
    val column: Byte,
    val row: Byte,
    val dataString: String?,
    val dataPercent: Float?
) {

    companion object {

        fun listFromDomain(elements: List<Element>, dataPoint: OneLineDataPoint?): List<ElementModel> {

            val map = elements.map { it to it.getData(dataPoint) }.sortedBy { it.second?.siValue() }

            val min = map.firstOrNull { it.second != null }?.second?.siValue()
            val max = map.lastOrNull { it.second != null }?.second?.siValue()

            val noScale = (min == null && max == null)

            return map.map {
                ElementModel(
                    atomicNumber = it.first.atomicNumber,
                    symbol = it.first.symbol,
                    column = it.first.tableColumn,
                    row = it.first.tableRow,
                    dataString = it.first.getDataString(dataPoint),
                    dataPercent = if (!noScale) it.second?.siValue()?.let { (it - min!!) / (max!! - min!!) } else null
                )
            }
        }

        private fun Element.getData(dataPoint: OneLineDataPoint?): Quantity? {
            return when (dataPoint) {
                OneLineDataPoint.ABUNDANCE_CRUST -> abundanceCrust
                OneLineDataPoint.ABUNDANCE_SEA -> abundanceSea
                OneLineDataPoint.ATOMIC_RADIUS -> atomicRadius
                OneLineDataPoint.ATOMIC_WEIGHT -> atomicWeight
                OneLineDataPoint.DISCOVERY_YEAR -> discoveryYear
                OneLineDataPoint.EN_PAULING -> enPauling
                OneLineDataPoint.VDW_RADIUS -> vdwRadius
                else -> null
            }
        }

        private fun Element.getDataString(dataPoint: OneLineDataPoint?): String {
            return when (dataPoint) {
                OneLineDataPoint.ABUNDANCE_CRUST -> abundanceCrust?.toString()
                OneLineDataPoint.ABUNDANCE_SEA -> abundanceSea?.toString()
                OneLineDataPoint.ATOMIC_RADIUS -> atomicRadius?.toString()
                OneLineDataPoint.ATOMIC_WEIGHT -> atomicWeight?.toString()
                OneLineDataPoint.DISCOVERY_LOCATION -> discoveryLocation
                OneLineDataPoint.DISCOVERY_YEAR -> discoveryYear?.toString()
                OneLineDataPoint.ELECTRONIC_CONFIGURATION -> electronicConfiguration
                OneLineDataPoint.EN_PAULING -> enPauling?.toString()
                OneLineDataPoint.NAME -> name
                OneLineDataPoint.VDW_RADIUS -> vdwRadius?.toString()
                else -> null
            } ?: ""
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