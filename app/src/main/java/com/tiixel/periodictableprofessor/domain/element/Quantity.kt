package com.tiixel.periodictableprofessor.domain.element

import org.jscience.physics.amount.Amount
import javax.measure.unit.Unit

data class Quantity(
    private val value: String,
    private val unit: Unit<*>
) {

    override fun toString(): String = "$value $unit"

    fun value(): Amount<*> = Amount.valueOf(value.toDouble(), unit).to(unit.standardUnit as Unit<*>)

    fun siValue(): Float = value().estimatedValue.toFloat()
}