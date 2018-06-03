package com.tiixel.periodictableprofessor.domain.card

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.tiixel.periodictableprofessor.domain.Card
import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.ReviewData
import com.tiixel.periodictableprofessor.domain.element.ElementRepository
import com.tiixel.periodictableprofessor.domain.exception.AllCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsDueSoonException
import com.tiixel.periodictableprofessor.factory.CardFactory
import com.tiixel.periodictableprofessor.factory.CardFactory.makeCard
import com.tiixel.periodictableprofessor.factory.DataFactory
import com.tiixel.periodictableprofessor.factory.DataFactory.Factory.makeAtomicNumber
import com.tiixel.periodictableprofessor.factory.ElementFactory
import com.tiixel.periodictableprofessor.factory.ReviewDataFactory
import com.tiixel.periodictableprofessor.factory.ReviewDataFactory.makePerfectReviewHistory
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Date

class CardInteractorImplTest {

    @Mock private lateinit var cardRepository: CardRepository
    @Mock private lateinit var elementRepository: ElementRepository

    private lateinit var cardInteractor: CardInteractorImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        cardInteractor = CardInteractorImpl(cardRepository, elementRepository)
    }

    //<editor-fold desc="getNewCardForReview()">
    @Test
    fun getNewCardForReview_errors_whenNoCardsAreNew() {
        stub_elementRepository_getElements()
        val reviewLog = ReviewDataFactory.makeReviewHistoryForElements(DataFactory.makeAtomicNumbers())
        stub_cardRepository_getReviewLog(Single.just(reviewLog))

        val testObserver = cardInteractor.getNewCardForReview().test()

        testObserver.assertError(NoCardsAreNewException)
    }

    @Test
    fun getNewCardForReview_retries_whenNoMnemonicForSelectedElement() {
        stub_elementRepository_getElements()
        stub_cardRepository_getReviewLog(Single.just(emptyList()))
        val element = DataFactory.makeAtomicNumber()
        val card1 = CardFactory.makeCard(element).copy(mnemonic = null)
        val card2 = CardFactory.makeCard(makeAtomicNumber(not = element))
        stub_cardRepository_getCard(Single.just(card1), Single.just(card2))

        val testObserver = cardInteractor.getNewCardForReview().test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue { it.first.element.atomicNumber != element }
    }

    @Test
    fun getNewCardForReview_returnsData() {
        stub_elementRepository_getElements()
        stub_cardRepository_getReviewLog(Single.just(emptyList()))
        val card = CardFactory.makeCard(DataFactory.makeAtomicNumber())
        stub_cardRepository_getCard(Single.just(card))

        val testObserver = cardInteractor.getNewCardForReview().test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue { it.first == card }
    }

    @Test
    fun getNewCardForReview_errors_onGetElementsRuntimeException() {
        val exception = RuntimeException()
        stub_elementRepository_getElements(Single.error(exception))
        stub_cardRepository_getReviewLog(Single.just(emptyList()))
        stub_cardRepository_getCard(Single.just(makeCard(makeAtomicNumber())))

        val testObserver = cardInteractor.getNewCardForReview().test()

        testObserver.assertError(exception)
    }

    @Test
    fun getNewCardForReview_errors_onGetReviewLogRuntimeException() {
        val exception = RuntimeException()
        stub_elementRepository_getElements()
        stub_cardRepository_getReviewLog(Single.error(exception))
        stub_cardRepository_getCard(Single.just(makeCard(makeAtomicNumber())))

        val testObserver = cardInteractor.getNewCardForReview().test()

        testObserver.assertError(exception)
    }

    @Test
    fun getNewCardForReview_errors_onGetCardException() {
        val exception = RuntimeException()
        stub_elementRepository_getElements()
        stub_cardRepository_getReviewLog(Single.just(emptyList()))
        stub_cardRepository_getCard(Single.error(exception))

        val testObserver = cardInteractor.getNewCardForReview().test()

        testObserver.assertError(exception)
    }
    //</editor-fold>

    //<editor-fold desc="getNextCardForReview()">
    @Test
    fun getNextCardForReview_errors_whenAllCardsAreNew() {
        stub_cardRepository_getReviewLog(Single.just(emptyList()))
        stub_cardRepository_getCard(Single.just(makeCard(makeAtomicNumber())))

        val testObserver = cardInteractor.getNextCardForReview().test()

        testObserver.assertError(AllCardsAreNewException)
    }

    @Test
    fun getNextCardForReview_errors_whenNoCardsDueSoon() {
        val review = makePerfectReviewHistory(1).first().copy(reviewDate = Date())
        stub_cardRepository_getReviewLog(Single.just(listOf(review)))
        stub_cardRepository_getCard(Single.just(makeCard(review.element)))

        val testObserver = cardInteractor.getNextCardForReview(dueSoonOnly = true).test()

        testObserver.assertError(NoCardsDueSoonException)
    }

    @Test
    fun getNextCardForReview_returnsData() {
        val reviews = makePerfectReviewHistory(10)
        stub_cardRepository_getReviewLog(Single.just(reviews))
        stub_cardRepository_getCard(Single.just(makeCard(reviews.first().element)))

        val testObserver = cardInteractor.getNextCardForReview(dueSoonOnly = false).test()

        testObserver.assertValue { it.first.element.atomicNumber == reviews.first().element }
    }

    @Test
    fun getNextCardForReview_errors_onGetLastReviewRuntimeException() {
        val exception = RuntimeException()
        stub_cardRepository_getReviewLog(Single.error(exception))
        stub_cardRepository_getCard(Single.just(makeCard(makeAtomicNumber())))

        val testObserver = cardInteractor.getNextCardForReview().test()

        testObserver.assertError(exception)
    }

    @Test
    fun getNextCardForReview_errors_onGetCardRuntimeException() {
        val exception = RuntimeException()
        stub_cardRepository_getReviewLog(Single.just(makePerfectReviewHistory(10)))
        stub_cardRepository_getCard(Single.error(exception))

        val testObserver = cardInteractor.getNextCardForReview().test()

        testObserver.assertError(exception)
    }
    //</editor-fold>

    //<editor-fold desc="countCardsDueToday()">
    @Test
    fun countCardsDueToday_returnsData() {
        stub_cardRepository_getReviewLog(Single.just(makePerfectReviewHistory(2)))

        val testObserver = cardInteractor.countCardsDueToday(Date(0)).test()

        testObserver.assertValue(1)
    }

    @Test
    fun countCardsDueToday_errors_onGetLastReviewRuntimeException() {
        val exception = RuntimeException()
        stub_cardRepository_getReviewLog(Single.error(exception))

        val testObserver = cardInteractor.countCardsDueToday(Date(0)).test()

        testObserver.assertError(exception)
    }
    //</editor-fold>

    // TODO:
    //<editor-fold desc="countCardsDueSoon()">
    @Test
    fun countCardsDueSoon_returnsData() {
    }

    @Test
    fun countCardsDueSoon_errors_onGetLastReviewRuntimeException() {
    }
    //</editor-fold>

    // TODO:
    //<editor-fold desc="countCardsNewOnDay()">
    @Test
    fun countCardsNewOnDay_returnsData() {
    }

    @Test
    fun countCardsNewOnDay_errors_onGetLastReviewRuntimeException() {
    }
    //</editor-fold>

    // TODO:
    //<editor-fold desc="reviewCard()">
    //</editor-fold>

    @Test
    fun learningCurve() {
        var time = Date(0L).time
        stub_cardRepository_getReviewLog(Single.just(emptyList()))
        stub_cardRepository_logReview(Completable.complete())

        cardInteractor.reviewCard(1, Card.Companion.Performance.FAILED, Date(time)).test()
        time += 600000
        cardInteractor.reviewCard(1, Card.Companion.Performance.MEDIUM, Date(time)).test()
        time += 1446000
        cardInteractor.reviewCard(1, Card.Companion.Performance.HARD, Date(time)).test()
        time += 3239000
        cardInteractor.reviewCard(1, Card.Companion.Performance.HARD, Date(time)).test()
        time += 6705000
        cardInteractor.reviewCard(1, Card.Companion.Performance.EASY, Date(time)).test()
        time += 14549000
        cardInteractor.reviewCard(1, Card.Companion.Performance.HARD, Date(time)).test()
        time += 33027000
        assert(false)
    }

    private fun stub_cardRepository_getReviewLog(single: Single<List<ReviewData>>) {
        whenever(cardRepository.getReviewLog())
            .thenReturn(single)
    }

    private fun stub_cardRepository_logReview(completable: Completable) {
        whenever(cardRepository.logReview(any()))
            .thenReturn(completable)
    }

    private fun stub_cardRepository_getCard(vararg singles: Single<Card>) {
        var ongoing = whenever(cardRepository.getCard(any()))
            .thenReturn(singles[0])
        (1 until singles.size).forEach {
            ongoing = ongoing.thenReturn(singles[it])
        }
    }

    private fun stub_elementRepository_getElements() {
        whenever(elementRepository.getElements())
            .thenReturn(Single.just(ElementFactory.makeElements()))
    }

    private fun stub_elementRepository_getElements(single: Single<List<Element>>) {
        whenever(elementRepository.getElements())
            .thenReturn(single)
    }
}