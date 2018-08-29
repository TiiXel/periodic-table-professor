package com.tiixel.periodictableprofessor.util.extensions

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

fun BarLineChartBase<*>.setXAxisMapValueFormatter(labels: Map<Float, String>) {
    xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
        if (labels.containsKey(value)) labels[value] else ""
    }
}

fun CombinedChart.groupBars() {

    val barData = barData

    val groupWidth =
        barData.getDataSetByIndex(0).getEntryForIndex(1).x - barData.getDataSetByIndex(0).getEntryForIndex(0).x
    val barWidth = groupWidth / barData.dataSetCount.toFloat()

    val newBarData = BarData()
    barData.dataSets.mapIndexed { index, barDataSet ->
        val newBarDataSet = BarDataSet(mutableListOf(), barDataSet.label)
        repeat(barDataSet.entryCount) {
            newBarDataSet.addEntry(barDataSet.getEntryForIndex(it).apply { x = x - groupWidth / 2f + index * barWidth })
        }
        newBarData.addDataSet(barDataSet)
    }

    newBarData.barWidth = barWidth
    data.setData(newBarData)

    xAxis.axisMinimum = data.xMin - groupWidth / 2f - barWidth / 2f
    xAxis.axisMaximum = data.xMax + groupWidth / 2f + barWidth / 2f
}

fun BarLineChartBase<*>.setSpecificPositionLabels(labels: Set<Float>) {
    setXAxisRenderer(
        SpecificPositionLabelsXAxisRenderer(
            viewPortHandler,
            xAxis,
            getTransformer(axisLeft.axisDependency),
            labels.toFloatArray(),
            true
        )
    )
}

/**
 * https://github.com/PhilJay/MPAndroidChart/pull/2692#issuecomment-378367864
 */
private class SpecificPositionLabelsXAxisRenderer(
    viewPortHandler: ViewPortHandler,
    xAxis: XAxis,
    trans: Transformer,
    private val specificLabelPositions: FloatArray,
    private val adjustLabelCountToChartWidth: Boolean
) : XAxisRenderer(viewPortHandler, xAxis, trans) {

    override fun computeAxisValues(min: Float, max: Float) {

        mAxis.mEntryCount = specificLabelPositions.size
        mAxis.mEntries = specificLabelPositions
        mAxis.setCenterAxisLabels(false)

        computeSize()

        if (adjustLabelCountToChartWidth && mViewPortHandler.chartWidth != 0f) {
            val width = mXAxis.mLabelRotatedWidth

            while (width * mAxis.mEntryCount > mViewPortHandler.chartWidth / 2f) {
                mAxis.mEntries = mAxis.mEntries.filterIndexed { index, fl -> index % 2 == 0 }.toFloatArray()
                mAxis.mEntryCount = mAxis.mEntries.size
            }
        }
    }
}