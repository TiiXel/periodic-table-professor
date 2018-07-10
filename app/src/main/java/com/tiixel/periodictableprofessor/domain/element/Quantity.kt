package com.tiixel.periodictableprofessor.domain.element

import org.jscience.physics.amount.Amount
import javax.measure.unit.Unit

/**
 * This class represents and holds a numerical value in a context where
 * significant figures are relevant, known, and should not be sensible to
 * the finite precision of computations. This number is accompanied by a
 * [`Unit`][Unit] which represents the physical unit in which it is expressed
 * (the latter can be set to [`Unit.ONE`][Unit.ONE]).
 *
 * @constructor Creates a [`Quantity`][Quantity].
 * @property value the decimal value of the `Quantity`, expressed either as an
 * integer or a real with a dot (`.`) as decimal separator, and no grouping
 * separator.
 * @property unit the [`Unit`][Unit] in which the [`value`][value] is expressed.
 */
data class Quantity(
    private val value: String,
    private val unit: Unit<*>
) {

    /**
     * Returns the [`value`][value] with its [`unit`][unit] formatted as
     * "value unit": the value is printed exactly like the string used to define
     * the [`Quantity`][Quantity], and is expressed in the same physical unit.
     *
     * Example:
     *
     *     val atomicMass = 18.998403163
     *     atomicMass.toString() + " u"
     *     >>> "18.998404 u"
     *
     *     val atomicMass = Quantity("18.998403163", NonSI.ATOMIC_MASS)
     *     atomicMass.toString()
     *     >>> "18.998403163 u"
     *
     * @see Unit
     * @see javax.measure.unit
     *
     * @return the value and its unit in a `string`.
     */
    override fun toString(): String = "$value $unit"

    /**
     * Returns the numerical [`value`][value] (`float`) of the
     * [`Quantity`][Quantity], as expressed in the base unit system derived from
     * the [`unit`][unit] used to define the `Quantity`.
     *
     * *The returned value is rounded at the finite precision of computations.*
     *
     * This is meant to be used when comparing quantities together (sort,
     * filter...)
     *
     * Example:
     *
     *     val atomicMass = 18.998403163
     *     atomicMass.siValue() + " u"
     *     >>> "18.998404 u"
     *
     * @see value
     * @see <a href="http://en.wikipedia.org/wiki/SI_base_unit">
     *       Wikipedia: SI base unit</a>
     *
     * @return the value (`float`) as expressed in the base unit system.
     */
    fun siValue(): Float =
        Amount.valueOf(value.toDouble(), unit).to(unit.standardUnit as Unit<*>).estimatedValue.toFloat()
}