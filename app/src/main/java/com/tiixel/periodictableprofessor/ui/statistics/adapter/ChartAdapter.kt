package com.tiixel.periodictableprofessor.ui.statistics.adapter

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.tiixel.periodictableprofessor.ui.statistics.model.ChartModel
import com.tiixel.periodictableprofessor.util.extensions.setSpecificPositionLabels
import com.tiixel.periodictableprofessor.util.extensions.setXAxisMapValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

object ChartAdapter {

    fun setDataOnChart(chart: LineChart, vararg chartModels: ChartModel) {

        if (chartModels.mapNotNull { it.data }.isEmpty()) {
            chart.clear()
            return
        }

        val barData = LineData()
        val xTicks = mutableMapOf<Float, String>()

        val sdf = SimpleDateFormat("dd MMM", Locale.UK)

        chartModels.forEach { model ->

            if (model.data != null) {

                val barDataSet = LineDataSet(mutableListOf(), model.title)
                barDataSet.color = model.color

                model.data.forEach {
                    xTicks[it.key.time.toFloat()] = sdf.format(it.key)
                    barDataSet.addEntry(Entry(it.key.time.toFloat(), it.value.toFloat()))
                }

                barData.addDataSet(barDataSet)
            }
        }

        chart.data = barData

        chart.data.setValueFormatter { _, _, _, _ -> "" }

        chart.setSpecificPositionLabels(xTicks.keys)
        chart.setXAxisMapValueFormatter(xTicks)

        chart.notifyDataSetChanged()
        chart.invalidate()
    }
}