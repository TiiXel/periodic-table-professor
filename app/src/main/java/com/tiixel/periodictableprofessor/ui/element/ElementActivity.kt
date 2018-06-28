package com.tiixel.periodictableprofessor.ui.element

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.presentation.base.MviView
import com.tiixel.periodictableprofessor.presentation.element.ElementIntent
import com.tiixel.periodictableprofessor.presentation.element.ElementViewModel
import com.tiixel.periodictableprofessor.presentation.element.ElementViewState
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
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
        when  {
            state.loadingInProgress -> {
            }
            state.loadingFailed -> {
            }
            state.element == null -> {
            }
            else -> {
                
            }
        }
    }
    //</editor-fold>
}