package com.tiixel.periodictableprofessor.ui.elementlist

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.presentation.base.MviView
import com.tiixel.periodictableprofessor.presentation.elementlist.ElementListIntent
import com.tiixel.periodictableprofessor.presentation.elementlist.ElementListViewModel
import com.tiixel.periodictableprofessor.presentation.elementlist.ElementListViewState
import com.tiixel.periodictableprofessor.presentation.elementlist.model.ElementModel
import com.tiixel.periodictableprofessor.ui.element.ElementActivity
import com.tiixel.periodictableprofessor.ui.elementlist.model.ElementCellModel
import com.tiixel.periodictableprofessor.widget.periodictable.PeriodicTableView
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.element_table_activity.*
import javax.inject.Inject

class ElementTableActivity : AppCompatActivity(), MviView<ElementListIntent, ElementListViewState> {

    // Intent publishers
    private val loadElementsIntentPublisher = PublishSubject.create<ElementListIntent.LoadElementListIntent>()

    // Used to manage the data flow lifecycle and avoid memory leak
    private val disposable = CompositeDisposable()

    // View references
    lateinit var table: PeriodicTableView

    // View model
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ElementListViewModel

    //<editor-fold desc="Life cycle methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.element_table_activity)
        AndroidInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ElementListViewModel::class.java)

        // Inflate views
        table = elements_table

        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.table_spinner_element_one_line_properties,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        elements_table_additional_data_spinner.adapter = spinnerAdapter
        elements_table_additional_data_spinner.onItemSelectedListener = SpinnerListener()

        // Subscribe to the ViewModel and call render for every emitted state
        disposable.add(viewModel.states().subscribe(this::render))
        // Pass the UI's intents to the ViewModel
        viewModel.processIntents(intents())
        // Subscribe to the PeriodicTableView observable
        disposable.add(table.elementClickedObservable.subscribe(this::onElementClicked))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
    //</editor-fold>

    //<editor-fold desc="MVI methods">
    override fun intents(): Observable<ElementListIntent> {
        return Observable.merge(
            Observable.just(ElementListIntent.InitialIntent),
            loadElementsIntentPublisher
        )
    }

    override fun render(state: ElementListViewState) {
        when {
            state.loadingInProgress -> {

            }
            state.loadingFailed -> {

            }
            state.elements.isEmpty() -> {

            }
            else -> {
                table.elements = ElementCellModel.listFromPresentation(state.elements)
            }
        }
    }
    //</editor-fold>

    private fun onElementClicked(element: Byte) {
        val intent = Intent(this, ElementActivity::class.java)
        intent.putExtra(ElementActivity.Extra.ELEMENT.key, element)
        startActivity(intent)
    }

    inner class SpinnerListener : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val dataPoint = when (position) {
                0 -> ElementModel.Companion.OneLineDataPoint.ABUNDANCE_CRUST
                1 -> ElementModel.Companion.OneLineDataPoint.ABUNDANCE_SEA
                2 -> ElementModel.Companion.OneLineDataPoint.ATOMIC_RADIUS
                3 -> ElementModel.Companion.OneLineDataPoint.ATOMIC_WEIGHT
                4 -> ElementModel.Companion.OneLineDataPoint.DISCOVERY_LOCATION
                5 -> ElementModel.Companion.OneLineDataPoint.DISCOVERY_YEAR
                6 -> ElementModel.Companion.OneLineDataPoint.ELECTRONIC_CONFIGURATION
                7 -> ElementModel.Companion.OneLineDataPoint.EN_PAULING
                8 -> ElementModel.Companion.OneLineDataPoint.NAME
                9 -> ElementModel.Companion.OneLineDataPoint.VDW_RADIUS
                else -> ElementModel.Companion.OneLineDataPoint.NAME
            }
            loadElementsIntentPublisher.onNext(ElementListIntent.LoadElementListIntent(dataPoint))
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }
}