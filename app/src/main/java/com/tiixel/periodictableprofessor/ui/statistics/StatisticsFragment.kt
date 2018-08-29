package com.tiixel.periodictableprofessor.ui.statistics

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.presentation.base.MviView
import com.tiixel.periodictableprofessor.presentation.home.statistics.StatisticsIntent
import com.tiixel.periodictableprofessor.presentation.home.statistics.StatisticsViewModel
import com.tiixel.periodictableprofessor.presentation.home.statistics.StatisticsViewState
import com.tiixel.periodictableprofessor.ui.statistics.adapter.ChartAdapter
import com.tiixel.periodictableprofessor.ui.statistics.model.ChartModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.content_statistics.*
import javax.inject.Inject

class StatisticsFragment : Fragment(), MviView<StatisticsIntent, StatisticsViewState> {

    // Used to manage the data flow lifecycle and avoid memory leak
    private val disposable = CompositeDisposable()

    // View model
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: StatisticsViewModel

    //<editor-fold desc="Life cycle methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StatisticsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Subscribe to the ViewModel and call render for every emitted state
        disposable.add(viewModel.states().subscribe(this::render))
        // Pass the UI's intents to the ViewModel
        viewModel.processIntents(intents())

        styleChart()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
    //</editor-fold>

    //<editor-fold desc="MVI methods">
    override fun intents(): Observable<StatisticsIntent> {
        return Observable.just(StatisticsIntent.InitialIntent)
    }

    override fun render(state: StatisticsViewState) {

        ChartAdapter.setDataOnChart(
            stats_chart,
            ChartModel("New on day", state.dataItemsNewPerDay, Color.parseColor("#ffff00")),
            ChartModel("Rev on day", state.dataReviewsPerDay, Color.parseColor("#e0b189")),
            ChartModel("Known at end of day", state.dataKnownReviewablesPerDay, Color.parseColor("#ab64c7")),
            ChartModel("Due before end of day", state.dataReviewsDuePerDay, Color.parseColor("#0000ff"))
        )

    }
    //</editor-fold>

    private fun styleChart() {
        stats_chart.apply {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisLeft.granularity = 1f
            axisLeft.isGranularityEnabled = true
            xAxis.setDrawGridLines(false)
            axisRight.isEnabled = false
            xAxis.labelRotationAngle = -90f
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        }
    }
}