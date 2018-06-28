package com.tiixel.periodictableprofessor.ui.element

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.presentation.base.MviView
import com.tiixel.periodictableprofessor.presentation.element.ElementIntent
import com.tiixel.periodictableprofessor.presentation.element.ElementViewModel
import com.tiixel.periodictableprofessor.presentation.element.ElementViewState
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.element_activity.*
import kotlinx.android.synthetic.main.view_element_property.view.*
import kotlinx.android.synthetic.main.view_element_property_group.view.*
import java.util.UUID
import javax.inject.Inject

class ElementActivity : AppCompatActivity(), MviView<ElementIntent, ElementViewState> {

    // Used to manage the data flow lifecycle and avoid memory leak
    private val disposable = CompositeDisposable()

    // View model
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ElementViewModel

    // Starting intent extra params
    enum class Extra(val key: String) {
        ELEMENT("element")
    }

    //<editor-fold desc="Life cycle methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.element_activity)
        AndroidInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ElementViewModel::class.java)

        // Subscribe to the ViewModel and call render for every emitted state
        disposable.add(viewModel.states().subscribe(this::render))
        // Pass the UI's intents to the ViewModel
        viewModel.processIntents(intents())
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
    //</editor-fold>

    //<editor-fold desc="MVI methods">
    override fun intents(): Observable<ElementIntent> {
        val element = intent.getByteExtra(Extra.ELEMENT.key, 0)
        return Observable.just(ElementIntent.InitialIntent(element))
    }

    override fun render(state: ElementViewState) {
        when {
            state.loadingInProgress -> {
            }
            state.loadingFailed -> {
            }
            state.element == null -> {
            }
            else -> {
                displayProperties(state.element)
            }
        }
    }
    //</editor-fold>

    private fun displayProperties(element: Element) {

        details_name.text = element.name
        details_atomic_number.text = element.atomicNumber.toString()
        details_symbol.text = element.symbol

        // General properties
        val generalGroup =
            layoutInflater.inflate(R.layout.view_element_property_group, element_properties, false) as LinearLayout
        generalGroup.element_property_group_icon.icon = IconicsDrawable(this)
            .icon(GoogleMaterial.Icon.gmd_description)
            .color(Color.BLACK)
        generalGroup.element_property_group_title.text = "General properties"

        val descriptionView = layoutInflater.inflate(R.layout.view_element_property, generalGroup, false) as LinearLayout
        descriptionView.property_title.text = getString(R.string.element_data_point_name_description)
        descriptionView.property_value.text = element.description
        generalGroup.addView(descriptionView)

        element_properties.addView(generalGroup)

        // Physical and chemical
        val physicalGroup =
            layoutInflater.inflate(R.layout.view_element_property_group, element_properties, false) as LinearLayout
        physicalGroup.element_property_group_icon.icon = IconicsDrawable(this)
            .icon(CommunityMaterial.Icon.cmd_temperature_celsius)
            .color(Color.BLACK)
        physicalGroup.element_property_group_title.text = "Physical & chemical properties"

        val electronicConfigurationView = layoutInflater.inflate(R.layout.view_element_property, physicalGroup, false) as LinearLayout
        electronicConfigurationView.property_title.text = getString(R.string.element_data_point_name_electronic_configuration)
        electronicConfigurationView.property_value.text = element.electronicConfiguration
        physicalGroup.addView(electronicConfigurationView)

        element_properties.addView(physicalGroup)

        // Mnemonic
        val mnemonicGroup =
            layoutInflater.inflate(R.layout.view_element_property_group, element_properties, false) as LinearLayout
        mnemonicGroup.element_property_group_icon.icon = IconicsDrawable(this)
            .icon(CommunityMaterial.Icon.cmd_lightbulb_on)
            .color(Color.BLACK)
        mnemonicGroup.element_property_group_title.text = "Mnemonic"

        val phraseView = layoutInflater.inflate(R.layout.view_element_property, mnemonicGroup, false) as LinearLayout
        phraseView.property_title.text = "Phrase"
        phraseView.property_value.text = UUID.randomUUID().toString()
        mnemonicGroup.addView(phraseView)

        element_properties.addView(mnemonicGroup)
    }
}