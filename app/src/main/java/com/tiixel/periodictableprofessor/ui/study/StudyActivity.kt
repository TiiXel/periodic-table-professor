package com.tiixel.periodictableprofessor.ui.study

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.domain.exception.NoNewReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewSoonException
import com.tiixel.periodictableprofessor.domain.review.ReviewPerformance
import com.tiixel.periodictableprofessor.domain.review.ReviewableFace
import com.tiixel.periodictableprofessor.presentation.base.MviView
import com.tiixel.periodictableprofessor.presentation.study.StudyIntent
import com.tiixel.periodictableprofessor.presentation.study.StudyViewModel
import com.tiixel.periodictableprofessor.presentation.study.StudyViewState
import com.tiixel.periodictableprofessor.ui.study.mapper.TimerParser
import com.tiixel.periodictableprofessor.widget.error.ErrorButton
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.study_activity.*
import ru.noties.markwon.Markwon
import javax.inject.Inject

class StudyActivity : AppCompatActivity(), MviView<StudyIntent, StudyViewState> {

    // Intent publishers
    private val loadNextIntentPublisher = PublishSubject.create<StudyIntent.LoadNextIntent>()
    private val reviewIntentPublisher = PublishSubject.create<StudyIntent.ReviewIntent>()
    private val checkIntentPublisher = PublishSubject.create<StudyIntent.CheckIntent>()

    // Used to manage the data flow lifecycle and avoid memory leak
    private val disposable = CompositeDisposable()

    // View model
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: StudyViewModel

    // View state
    private lateinit var viewState: StudyViewState

    // Starting intent extra params
    enum class Extra(val key: String) {
        NEW_CARDS("new_cards"),
        DUE_TODAY_ONLY("due_today_only")
    }

    private var reviewNewCards: Boolean = true
    private var reviewDueTodayOnly: Boolean = false

    //<editor-fold desc="Life cycle methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_activity)
        setSupportActionBar(toolbar)
        AndroidInjection.inject(this)

        supportActionBar!!.run {
            setDisplayHomeAsUpEnabled(true)
        }

        reviewNewCards = intent?.extras?.getBoolean(Extra.NEW_CARDS.key) ?: reviewNewCards
        reviewDueTodayOnly = intent?.extras?.getBoolean(Extra.DUE_TODAY_ONLY.key) ?: reviewDueTodayOnly

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StudyViewModel::class.java)

        review_periodic_table.generateBlankCells()

        setupViewListeners()

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
    override fun intents(): Observable<StudyIntent> {
        return Observable.merge(
            Observable.just(StudyIntent.InitialIntent(newCard = reviewNewCards, dueSoonOnly = reviewDueTodayOnly)),
            loadNextIntentPublisher,
            reviewIntentPublisher,
            checkIntentPublisher
        )
    }

    override fun render(state: StudyViewState) {
        viewState = state

        // Display quick statistics in header
        review_appbar_extra.text = getString(
            R.string.review_appbar_extra_counts,
            state.dueSoonCount,
            state.dueTodayCount
        )

        // Show loading bar
        if (state.loadingInProgress || state.reviewingInProgress) {
            review_loading_bar.visibility = View.VISIBLE
        } else {
            review_loading_bar.visibility = View.GONE
        }

        // Handle loading errors
        when (state.loadingFailedCause) {
            null -> {
                review_card.visibility = View.VISIBLE
                review_buttons.visibility = View.VISIBLE
                hideError()
            }
            is NoNextReviewException -> {
                showError(
                    getString(R.string.error_message_all_cards_are_new),
                    ErrorButton(getString(R.string.error_message_all_cards_are_new_button), loadNewCard)
                )
            }
            is NoNextReviewSoonException -> {
                showError(
                    getString(
                        R.string.error_message_no_cards_due_soon,
                        TimerParser.timerToString(this, state.nextReviewTimer),
                        state.newCardCount
                    ),
                    ErrorButton(getString(R.string.error_message_no_cards_due_soon_button_load_new), loadNewCard),
                    ErrorButton(getString(R.string.error_message_no_cards_due_soon_button_reload), reload)
                )
            }
            is NoNewReviewException -> {
                showError(getString(R.string.error_message_no_cards_are_new))
            }
            else -> {
                showError(
                    getString(R.string.error_message_default)
                )
            }
        }

        // Handle reviewing errors
        if (state.reviewingFailed) {
        }

        // Display the element
        if (state.element != null) {

            review_atomic_number.text = state.element.number
            review_atomic_number.visibility = if (state.isNumberVisible) View.VISIBLE else View.INVISIBLE

            review_symbol.text = state.element.symbol
            review_symbol.visibility = if (state.isSymbolVisible) View.VISIBLE else View.INVISIBLE

            review_name.text = state.element.name
            review_name.visibility = if (state.isNameVisible) View.VISIBLE else View.INVISIBLE

            review_mnemonic_picture.setImageBitmap(state.element.mnemonicPicture)
            review_mnemonic_picture.visibility = if (state.isPictureVisible) View.VISIBLE else View.INVISIBLE

            Markwon.setMarkdown(review_mnemonic_phrase, state.element.mnemonicPhrase ?: "")
            review_mnemonic_phrase.visibility = if (state.isPhraseVisible) View.VISIBLE else View.INVISIBLE

            if (state.isTablePositionVisible && state.itemId != null) {
                review_periodic_table.colorizeElement(state.itemId)
            } else {
                review_periodic_table.generateBlankCells()
            }

            review_user_note.text = state.element.userNote
            review_user_note.visibility = if (state.isUserNoteVisible) View.VISIBLE else View.INVISIBLE

            if (state.element.userNote.isNullOrEmpty()) {
                review_user_note.visibility = View.GONE
                review_button_edit_note.text = resources.getString(R.string.add_note)
            } else {
                review_user_note.visibility = View.VISIBLE
                review_button_edit_note.text = resources.getString(R.string.edit_note)
            }
        }

        // Show buttons
        if (state.showCheckButtonOverPerformance) {
            review_buttons_performance.visibility = View.GONE
            review_button_check.visibility = View.VISIBLE
        } else {
            review_buttons_performance.visibility = View.VISIBLE
            review_button_check.visibility = View.GONE
        }
    }
    //</editor-fold>

    private fun setupViewListeners() {
        review_button_check.setOnClickListener {
            checkCard()
        }
        review_button_easy.setOnClickListener {
            reviewCard(ReviewPerformance.EASY)
        }
        review_button_medium.setOnClickListener {
            reviewCard(ReviewPerformance.MEDIUM)
        }
        review_button_hard.setOnClickListener {
            reviewCard(ReviewPerformance.HARD)
        }
        review_button_failed.setOnClickListener {
            reviewCard(ReviewPerformance.FAILED)
        }
    }

    private fun hideError() {
        review_error.visibility = View.GONE
        review_error.hide()
    }

    private fun showError(message: String, vararg buttons: ErrorButton) {
        if (buttons.size > 3) throw IllegalArgumentException("You cannot pass more than 3 buttons")
        review_card.visibility = View.GONE
        review_buttons.visibility = View.GONE
        review_error.show(message, *buttons)
    }

    private fun showError(message: String) {
        review_card.visibility = View.GONE
        review_buttons.visibility = View.GONE
        review_error.show(message)
    }

    //<editor-fold desc="Intent publisher methods">
    private fun loadNextCard(forceNewCard: Boolean = false) {
        val intent =
            StudyIntent.LoadNextIntent(newCard = reviewNewCards || forceNewCard, dueSoonOnly = reviewDueTodayOnly)
        loadNextIntentPublisher.onNext(intent)
    }

    private fun checkCard() {
        val intent = StudyIntent.CheckIntent
        checkIntentPublisher.onNext(intent)
    }

    private fun reviewCard(performance: ReviewPerformance) {
        viewState.element?.let {
            val intent = StudyIntent.ReviewIntent(it.number.toByte(), ReviewableFace.SYMBOL, performance)
            reviewIntentPublisher.onNext(intent)
        } ?: throw RuntimeException("You cannot review a null card.")
        loadNextCard()
    }
    //</editor-fold>

    //<editor-fold desc="Intent publisher methods">
    private val loadNewCard = {
        loadNextCard(forceNewCard = true)
    }

    private val reload = {
        loadNextCard()
    }
    //</editor-fold>
}