package com.tiixel.periodictableprofessor.ui.review

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.domain.Card
import com.tiixel.periodictableprofessor.domain.exception.AllCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsDueSoonException
import com.tiixel.periodictableprofessor.presentation.base.MviView
import com.tiixel.periodictableprofessor.presentation.review.ReviewIntent
import com.tiixel.periodictableprofessor.presentation.review.ReviewViewModel
import com.tiixel.periodictableprofessor.presentation.review.ReviewViewState
import com.tiixel.periodictableprofessor.ui.review.mapper.TimerParser
import com.tiixel.periodictableprofessor.widget.error.ErrorButton
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.review_activity.*
import ru.noties.markwon.Markwon
import javax.inject.Inject

class ReviewActivity : AppCompatActivity(), MviView<ReviewIntent, ReviewViewState> {

    // Intent publishers
    private val loadNextCardIntentPublisher = PublishSubject.create<ReviewIntent.LoadNextCardIntent>()
    private val reviewCardIntentPublisher = PublishSubject.create<ReviewIntent.ReviewCardIntent>()
    private val checkCardIntentPublisher = PublishSubject.create<ReviewIntent.CheckCardIntent>()

    // Used to manage the data flow lifecycle and avoid memory leak
    private val disposable = CompositeDisposable()

    // View model
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ReviewViewModel

    // View state
    private lateinit var viewState: ReviewViewState

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
        setContentView(R.layout.review_activity)
        setSupportActionBar(toolbar)
        AndroidInjection.inject(this)

        supportActionBar!!.run {
            setDisplayHomeAsUpEnabled(true)
        }

        reviewNewCards = intent?.extras?.getBoolean(Extra.NEW_CARDS.key) ?: reviewNewCards
        reviewDueTodayOnly = intent?.extras?.getBoolean(Extra.DUE_TODAY_ONLY.key) ?: reviewDueTodayOnly

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ReviewViewModel::class.java)

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
    override fun intents(): Observable<ReviewIntent> {
        return Observable.merge(
            Observable.just(ReviewIntent.InitialIntent(newCard = reviewNewCards, dueSoonOnly = reviewDueTodayOnly)),
            loadNextCardIntentPublisher,
            reviewCardIntentPublisher,
            checkCardIntentPublisher
        )
    }

    override fun render(state: ReviewViewState) {
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
            is AllCardsAreNewException -> {
                showError(
                    getString(R.string.error_message_all_cards_are_new),
                    ErrorButton(getString(R.string.error_message_all_cards_are_new_button), loadNewCard)
                )
            }
            is NoCardsDueSoonException -> {
                showError(
                    getString(
                        R.string.error_message_no_cards_due_soon,
                        TimerParser.timerToString(this, state.nextReviewTimer)
                    ),
                    ErrorButton(getString(R.string.error_message_no_cards_due_soon_button_load_new), loadNewCard),
                    ErrorButton(getString(R.string.error_message_no_cards_due_soon_button_reload), reload)
                )
            }
            is NoCardsAreNewException -> {
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

        // Display the card
        if (state.card != null) {

            review_atomic_number.text = state.card.number
            review_atomic_number.visibility = if (state.card.visibleNumber) View.VISIBLE else View.INVISIBLE

            review_symbol.text = state.card.symbol
            review_symbol.visibility = if (state.card.visibleSymbol) View.VISIBLE else View.INVISIBLE

            review_name.text = state.card.name
            review_name.visibility = if (state.card.visibleName) View.VISIBLE else View.INVISIBLE

            review_mnemonic_picture.visibility = if (state.card.visibleMnemonicPicture) View.VISIBLE else View.INVISIBLE

            Markwon.setMarkdown(review_mnemonic_phrase, state.card.mnemonicPhrase)
            review_mnemonic_phrase.visibility = if (state.card.visibleMnemonicPhrase) View.VISIBLE else View.INVISIBLE

            review_periodic_table.colorizeElement(state.card.number.toByte())

            review_user_note.text = state.card.userNote
            review_user_note.visibility = if (state.card.visibleUserNote) View.VISIBLE else View.INVISIBLE

            review_mnemonic_picture.setImageBitmap(state.card.mnemonicPicture)

            if (state.card.userNote.isEmpty()) {
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
            reviewCard(Card.Companion.Performance.EASY)
        }
        review_button_medium.setOnClickListener {
            reviewCard(Card.Companion.Performance.MEDIUM)
        }
        review_button_hard.setOnClickListener {
            reviewCard(Card.Companion.Performance.HARD)
        }
        review_button_failed.setOnClickListener {
            reviewCard(Card.Companion.Performance.FAILED)
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
            ReviewIntent.LoadNextCardIntent(newCard = reviewNewCards || forceNewCard, dueSoonOnly = reviewDueTodayOnly)
        loadNextCardIntentPublisher.onNext(intent)
    }

    private fun checkCard() {
        val intent = ReviewIntent.CheckCardIntent
        checkCardIntentPublisher.onNext(intent)
    }

    private fun reviewCard(performance: Card.Companion.Performance) {
        viewState.card?.let {
            val intent = ReviewIntent.ReviewCardIntent(it.number.toByte(), performance)
            reviewCardIntentPublisher.onNext(intent)
        } ?: throw RuntimeException("You cannot review a null card: ${viewState.card}")
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