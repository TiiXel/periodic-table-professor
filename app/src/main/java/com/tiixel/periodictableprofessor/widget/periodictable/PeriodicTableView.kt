package com.tiixel.periodictableprofessor.widget.periodictable

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.ui.elementlist.model.ElementCellModel
import kotlin.math.min
import kotlin.math.roundToInt

class PeriodicTableView(context: Context, attrs: AttributeSet?, style: Int) : View(context, attrs, style) {

    /**
     * The list of [PeriodicTableCellModel] to be displayed in the [PeriodicTableView].
     *
     * The size of this list has to be 118.
     */
    var elements: List<ElementCellModel> = emptyList()
        set(value) {
            if (value.size == 118 && value.distinctBy { it.number }.size == 118 && value.filter { it.number in (1..118) }.size == 118) {
                field = value.sortedBy { it.number }
            } else {
                throw IllegalArgumentException("Value of elements should be a list of size 118")
            }
            invalidate()
        }

    fun generateBlankCells() {
        elements = List(118, {
            ElementCellModel(
                (it + 1).toByte(),
                "",
                "",
                Element.tableColumnOf((it + 1).toByte()),
                Element.tableRowOf((it + 1).toByte()),
                ElementCellModel.colorArray[0]
            )
        })
        numberPaint.color = Color.TRANSPARENT
    }

    fun colorizeElement(z: Byte) {
        elements = elements.map {
            if (it.number == z) {
                it.copy(backgroundColor = ElementCellModel.colorArray[8])
            } else {
                it.copy(backgroundColor = ElementCellModel.colorArray[0])
            }
        }
    }

    /**
     * Whether or not to draw the [PeriodicTableCellModel.number], [PeriodicTableCellModel.symbol], [PeriodicTableCellModel.additionalInfo]
     */
    private val drawDetails: Boolean

    /**
     * The atmoicNumber of column in the periodic table.
     */
    private val colCount = 18
    /**
     * The of atmoicNumber of row in the periodic table.
     */
    private val rowCount = 9

    /**
     * The size of a cell in the [PeriodicTableView].
     */
    private var cellSize: Float = 32f
    /**
     * The margin between cells.
     */
    private val cellMargin: Float = 1f
    /**
     * The inner padding in each cell.
     */
    private val cellPadding = 2f

    /**
     * The background color of each [PeriodicTableCellModel]'s cell.
     *
     * Defined here to avoid instantiation in [onDraw]
     */
    private var cellPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
    }
    /**
     * The color used to draw the [PeriodicTableCellModel.symbol].
     *
     * Defined here to avoid instantiation in [onDraw]
     */
    private var symbolPaint = Paint().apply {
        color = ResourcesCompat.getColor(resources, android.R.color.primary_text_light, context.theme)
        isSubpixelText = true
    }
    /**
     * The color used to draw the [PeriodicTableCellModel.number].
     *
     * Defined here to avoid instantiation in [onDraw]
     */
    private var numberPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        color = ResourcesCompat.getColor(resources, android.R.color.primary_text_light, context.theme)
        isSubpixelText = true
    }
    /**
     * The color used to draw the [PeriodicTableCellModel.additionalInfo].
     *
     * Defined here to avoid instantiation in [onDraw]
     */
    private var infoPaint = Paint()

    /**
     * The margin between the main table and the f block.
     */
    private var fBlockSpace: Int = 8

    /**
     * The background color of the [PeriodicTableView].
     */
    private val backgroundPaint =
        Paint(Paint.ANTI_ALIAS_FLAG + Paint.FILTER_BITMAP_FLAG).apply { color = Color.TRANSPARENT }

    /**
     * Reusable field, used to avoid instantiation in [onDraw]
     */
    private val reusableRect = Rect()
    /**
     * Reusable field, used to avoid instantiation in [onDraw]
     */
    private var reusableString = String()

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PeriodicTableView)
        val fitX = typedArray.getBoolean(R.styleable.PeriodicTableView_fitX, true)
        val fitY = typedArray.getBoolean(R.styleable.PeriodicTableView_fitY, true)
        val zoomable = typedArray.getBoolean(R.styleable.PeriodicTableView_zoomable, false)
        drawDetails = typedArray.getBoolean(R.styleable.PeriodicTableView_drawDetails, true)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize: Int
        val heightSize: Int

        val aspectRatio = rowCount.toFloat() / colCount.toFloat()

        val horizontalPadding = paddingLeft + paddingRight
        val verticalPadding = paddingTop + paddingBottom

        // Measure width
        val desiredWidth = 1000
        widthSize = when (widthMode) {
            MeasureSpec.EXACTLY -> // Must be this size
                MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST -> // Can't be bigger than
                Math.min(desiredWidth, MeasureSpec.getSize(widthMeasureSpec))
            MeasureSpec.UNSPECIFIED -> // Whatever
                desiredWidth
            else -> desiredWidth
        }

        val drawableWidth = widthSize - horizontalPadding
        val drawableHeight = drawableWidth * aspectRatio + fBlockSpace

        // Measure height
        val desiredHeight = (drawableHeight + verticalPadding).roundToInt()
        heightSize = when (heightMode) {
            MeasureSpec.EXACTLY -> // Must be this size
                MeasureSpec.getSize(heightMeasureSpec)
            MeasureSpec.AT_MOST -> // Can't be bigger than
                Math.min(desiredHeight, MeasureSpec.getSize(heightMeasureSpec))
            MeasureSpec.UNSPECIFIED -> // Whatever
                desiredHeight
            else -> desiredHeight
        }


        cellSize = (drawableWidth.toFloat() / colCount.toFloat())
        fBlockSpace = (cellSize / 3f).roundToInt()

        symbolPaint.textSize = cellSize * 0.4f
        numberPaint.textSize = cellSize * 0.2f

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), backgroundPaint)

        var cellTop: Float
        var cellBottom: Float
        var cellLeft: Float
        var cellRight: Float

        elements.forEach { element ->

            // Compute cell vertices positions
            cellLeft = element.column * cellSize + cellMargin / 2f + paddingLeft
            cellTop = element.row * cellSize + (if (element.row > 6) fBlockSpace else 0) + cellMargin / 2f + paddingTop
            cellRight = cellLeft + cellSize - cellMargin
            cellBottom = cellTop + cellSize - cellMargin

            // Draw cell background
            cellPaint.color = Color.parseColor(element.backgroundColor)
            canvas.drawRect(cellLeft, cellTop, cellRight, cellBottom, cellPaint)

            if (drawDetails) {

                // Draw symbol
                reusableString = element.symbol
                symbolPaint.getTextBounds(reusableString, 0, reusableString.length, reusableRect)
                canvas.drawText(
                    reusableString,
                    cellLeft + cellSize / 2f - reusableRect.exactCenterX(),
                    cellTop + cellSize / 2f - reusableRect.exactCenterY(),
                    symbolPaint
                )

                // Draw atmoicNumber
                // TODO: Numbers are not evenly placed across the table, some are higher up
                reusableString = element.number.toString()
                numberPaint.getTextBounds(reusableString, 0, reusableString.length, reusableRect)
                canvas.drawText(
                    reusableString, cellLeft + cellPadding, cellTop + reusableRect.height() + cellPadding, numberPaint
                )

                // Draw additional info
                infoPaint = numberPaint
                reusableString = element.data
                infoPaint.getTextBounds(reusableString, 0, reusableString.length, reusableRect)
                infoPaint.textSize = min(
                    infoPaint.textSize,
                    infoPaint.textSize * (cellRight - cellLeft - 2 * cellPadding) / reusableRect.width()
                )
                canvas.drawText(
                    reusableString,
                    cellLeft + cellSize / 2f - reusableRect.exactCenterX(),
                    cellBottom - cellPadding,
                    infoPaint
                )
            }
        }
    }
}