package com.tiixel.periodictableprofessor.domain.review

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.domain.exception.NoNewReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewException
import com.tiixel.periodictableprofessor.domain.exception.NoNextReviewSoonException
import com.tiixel.periodictableprofessor.domain.review.contract.ReviewRepository
import com.tiixel.periodictableprofessor.domain.review.interactor.ReviewInteractorImpl
import com.tiixel.periodictableprofessor.test.MockReview.dTimePast
import com.tiixel.periodictableprofessor.test.MockReview.dTimeSoon
import com.tiixel.periodictableprofessor.test.MockReview.e1earl
import com.tiixel.periodictableprofessor.test.MockReview.e1later
import com.tiixel.periodictableprofessor.test.MockReview.e1past
import com.tiixel.periodictableprofessor.test.MockReview.e1pastCram
import com.tiixel.periodictableprofessor.test.MockReview.e1soon
import com.tiixel.periodictableprofessor.test.MockReview.e2earl
import com.tiixel.periodictableprofessor.test.MockReview.e2later
import com.tiixel.periodictableprofessor.test.MockReview.e2past
import com.tiixel.periodictableprofessor.test.MockReview.e2soon
import com.tiixel.periodictableprofessor.test.MockReview.e3later
import com.tiixel.periodictableprofessor.test.MockReview.e3past
import com.tiixel.periodictableprofessor.test.MockReview.e3soon
import com.tiixel.periodictableprofessor.test.MockReview.makeMockReview
import com.tiixel.periodictableprofessor.test.MockReview.min
import com.tiixel.periodictableprofessor.test.MockReview.now
import com.tiixel.periodictableprofessor.test.MockReview.rTimeLater
import com.tiixel.periodictableprofessor.test.MockReview.rTimeMiss
import com.tiixel.periodictableprofessor.test.MockReview.rTimePast
import com.tiixel.periodictableprofessor.test.MockReview.rTimeSoon
import io.reactivex.Completable
import io.reactivex.Single
import org.apache.commons.lang3.time.DateUtils
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Calendar
import java.util.Date

class ReviewInteractorImplTest {

    @Mock private lateinit var reviewRepository: ReviewRepository
    @Mock private lateinit var elementInteractor: ElementInteractor
    private lateinit var reviewInteractor: ReviewInteractorImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        reviewInteractor = ReviewInteractorImpl(reviewRepository, elementInteractor)
    }

    // getReviewForNew()
    @Test
    fun getReviewForNew_returnsData() {
        stub_elementRepository_getReviewableIds(Single.just(listOf<Byte>(1, 2, 3)))
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e1earl, e1soon, e2past)))

        val testObserver = reviewInteractor.getReviewForNew().test()

        testObserver.assertValue { it.item.itemId == 3.toByte() }
    }

    @Test
    fun getReviewForNew_errors_whenNoNewReview() {
        stub_elementRepository_getReviewableIds(Single.just(listOf<Byte>(1, 2)))
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e1earl, e1soon, e2past)))

        val testObserver = reviewInteractor.getReviewForNew().test()

        testObserver.assertError(NoNewReviewException)
    }

    @Test
    fun getReviewForNew_errors_whenRuntimeException_inGetReviewableIds() {
        val exception = RuntimeException()
        stub_elementRepository_getReviewableIds(Single.just(listOf<Byte>(1, 2)))
        stub_reviewRepository_getReviewHistory(Single.error(exception))

        val testObserver = reviewInteractor.getReviewForNew().test()

        testObserver.assertError(exception)
    }

    @Test
    fun getReviewForNew_errors_whenRuntimeException_inGetReviewHistory() {
        val exception = RuntimeException()
        stub_elementRepository_getReviewableIds(Single.error(exception))
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past)))

        val testObserver = reviewInteractor.getReviewForNew().test()

        testObserver.assertError(exception)
    }

    // getReviewForNext()
    @Test
    fun getReviewForNext_dueSoonOnlyTrue_returnsData_whenOverdue() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e2past, e2earl, e2soon, e3soon)))

        val testObserver = reviewInteractor.getReviewForNext(dueSoonOnly = true).test()

        testObserver.assertValue { it.item.itemId == 1.toByte() }
    }

    @Test
    fun getReviewForNext_dueSoonOnlyTrue_returnsData_whenNoOverdue() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e1soon, e1later, e2later, e3past, e3soon)))

        val testObserver = reviewInteractor.getReviewForNext(dueSoonOnly = true).test()

        testObserver.assertValue { it.item.itemId == 3.toByte() }
    }

    @Test
    fun getReviewForNext_dueSoonOnlyTrue_errors_whenNothing() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf()))

        val testObserver = reviewInteractor.getReviewForNext(dueSoonOnly = true).test()

        testObserver.assertError(NoNextReviewException)
    }

    @Test
    fun getReviewForNext_dueSoonOnlyTrue_errors_whenNoSoon() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e1soon, e1later, e2past, e2soon, e2later)))

        val testObserver = reviewInteractor.getReviewForNext(dueSoonOnly = true).test()

        testObserver.assertError(NoNextReviewSoonException)
    }

    @Test
    fun getReviewForNext_dueSoonOnlyFalse_returnsData_whenNoSoon() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1soon, e1later, e2later, e3past, e3soon, e3later)))

        val testObserver = reviewInteractor.getReviewForNext(dueSoonOnly = false).test()

        testObserver.assertValue { it.item.itemId == 1.toByte() }
    }

    @Test
    fun getReviewForNext_dueSoonOnlyFalse_errors_whenNothing() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf()))

        val testObserver = reviewInteractor.getReviewForNext(dueSoonOnly = true).test()

        testObserver.assertError(NoNextReviewException)
    }

    @Test
    fun getReviewForNext_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_reviewRepository_getReviewHistory(Single.error(exception))

        val testObserver = reviewInteractor.getReviewForNext().test()

        testObserver.assertError(exception)
    }

    // getNextReviewDate()
    @Test
    fun getNextReviewDate_returnsData() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e1soon, e1later, e2past, e2soon)))

        val testObserver = reviewInteractor.getNextReviewDate().test()

        testObserver.assertValue(e2soon.nextDueDate)
    }

    @Test
    fun getNextReviewDate_errors_whenNothing() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf()))

        val testObserver = reviewInteractor.getNextReviewDate().test()

        testObserver.assertError(NoNextReviewException)
    }

    @Test
    fun getNextReviewDate_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_reviewRepository_getReviewHistory(Single.error(exception))

        val testObserver = reviewInteractor.getNextReviewDate().test()

        testObserver.assertError(exception)
    }

    // countReviewsDuePerPeriod()
    @Test
    fun countReviewsDuePerPeriod_returnsData() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e1pastCram, e1soon, e2past, e3past, e3later)))

        val testObserver = reviewInteractor.countReviewsDuePerPeriod(Calendar.DATE).test()

        testObserver.assertValue(
            mapOf(
                Pair(DateUtils.truncate(Date(dTimePast), Calendar.DATE), 3),
                Pair(DateUtils.truncate(Date(rTimeMiss), Calendar.DATE), 0),
                Pair(DateUtils.truncate(Date(dTimeSoon), Calendar.DATE), 2)
            )
        )
    }

    @Test
    fun countReviewsDuePerPeriod_returnsData_whenNothing() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf()))

        val testObserver = reviewInteractor.countReviewsDuePerPeriod(Calendar.DATE).test()

        testObserver.assertValue(emptyMap())
    }

    @Test
    fun countReviewsDuePerPeriod_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_reviewRepository_getReviewHistory(Single.error(exception))

        val testObserver = reviewInteractor.countReviewsDuePerPeriod(Calendar.DATE).test()

        testObserver.assertError(exception)
    }

    // countReviewsDueSoon()
    @Test
    fun countReviewsDueSoon_returnsData() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e1soon, e2past, e3past, e3later)))

        val testObserver = reviewInteractor.countReviewsDueSoon(now).test()

        testObserver.assertValue(2)
    }

    @Test
    fun countReviewsDueSoon_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_reviewRepository_getReviewHistory(Single.error(exception))

        val testObserver = reviewInteractor.countReviewsDueSoon(Date(min)).test()

        testObserver.assertError(exception)
    }

    // countReviewsOnDay()
    @Test
    fun countReviewsPerPeriod_returnsData() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e1soon, e2past, e2later, e3later)))

        val testObserver = reviewInteractor.countReviewsPerPeriod(Calendar.DATE).test()

        testObserver.assertValue(
            mapOf(
                Pair(DateUtils.truncate(Date(rTimePast), Calendar.DATE), 2),
                Pair(DateUtils.truncate(Date(rTimeMiss), Calendar.DATE), 0),
                Pair(DateUtils.truncate(Date(rTimeSoon), Calendar.DATE), 3)
            )
        )
    }

    @Test
    fun countReviewsPerPeriod_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_reviewRepository_getReviewHistory(Single.error(exception))

        val testObserver = reviewInteractor.countReviewsPerPeriod(Calendar.DATE).test()

        testObserver.assertError(exception)
    }

    // countReviewablesNewPerPeriod()
    @Test
    fun countReviewablesNewPerPeriod_returnsData() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past, e1soon, e1later, e2past, e3later)))

        val testObserver = reviewInteractor.countReviewablesNewPerPeriod(Calendar.DATE).test()

        testObserver.assertValue(
            mapOf(
                Pair(DateUtils.truncate(Date(rTimePast), Calendar.DATE), 2),
                Pair(DateUtils.truncate(Date(rTimeMiss), Calendar.DATE), 0),
                Pair(DateUtils.truncate(Date(rTimeLater), Calendar.DATE), 1)
            )
        )
    }

    @Test
    fun countReviewablesNewPerPeriod_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_reviewRepository_getReviewHistory(Single.error(exception))

        val testObserver = reviewInteractor.countReviewablesNewPerPeriod(Calendar.DATE).test()

        testObserver.assertError(exception)
    }

    // countKnownReviewablesPerPeriod()
    @Test
    fun countKnownReviewablesPerPeriod_returnsData() {
        val now = Date().time
        val knownThresholdTimer = 40000000L
        val e1Known = makeMockReview(1, now + 0, now + knownThresholdTimer + 100)
        val e1Unknown = makeMockReview(1, now + 100, now + knownThresholdTimer + 100 - 100)
        val e2Known = makeMockReview(2, now + 0, now + knownThresholdTimer + 100)
        val e3Unknown = makeMockReview(3, now + 0, now + knownThresholdTimer - 100)
        val e3Known1 = makeMockReview(3, now + 100, now + knownThresholdTimer + 100 + 100)
        val e3Known2 = makeMockReview(3, now + 100 + 100, now + knownThresholdTimer + 100 + 100 + 100)
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1Known, e1Unknown, e2Known, e3Unknown, e3Known1, e3Known2)))

        val testObserverDate = reviewInteractor.countKnownReviewablesPerPeriod(Calendar.DATE).test()

        testObserverDate.assertValue { value ->
            DateUtils.truncate(Date(now), Calendar.DATE).let { value.containsKey(it) && value[it] == 2 }
        }

        val testObserverSecond = reviewInteractor.countKnownReviewablesPerPeriod(Calendar.MILLISECOND).test()

        testObserverSecond.assertValue { value ->
            DateUtils.truncate(Date(now), Calendar.MILLISECOND).let { value.containsKey(it) && value[it] == 2 }
                && DateUtils.truncate(Date(now + 100), Calendar.MILLISECOND).let { value.containsKey(it) && value[it] == 2 }
                && DateUtils.truncate(Date(now + 200), Calendar.MILLISECOND).let { value.containsKey(it) && value[it] == 2 }
                && DateUtils.truncate(Date(), Calendar.MILLISECOND).let { value.containsKey(it) && value[it] == 0 }
        }
    }

    @Test
    fun countKnownReviewablesPerPeriod_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_reviewRepository_getReviewHistory(Single.error(exception))

        val testObserver = reviewInteractor.countKnownReviewablesPerPeriod(Calendar.DATE).test()

        testObserver.assertError(exception)
    }

    // review()
    @Test
    fun review_completes() {
        stub_reviewRepository_getReviewHistory(Single.just(listOf()))
        stub_reviewRepository_logReview(Completable.complete())
        val review = Review.FreshReview(Reviewable(1), ReviewableFace.PICTURE, Date(), ReviewPerformance.FAILED)

        val testObserver = reviewInteractor.review(review).test()

        testObserver.assertComplete()
    }

    @Test
    fun review_errors_whenRuntimeException_inGetReviewHistory() {
        val exception = RuntimeException()
        stub_reviewRepository_getReviewHistory(Single.error(exception))
        stub_reviewRepository_logReview(Completable.complete())
        val review = Review.FreshReview(Reviewable(1), ReviewableFace.PICTURE, Date(), ReviewPerformance.FAILED)

        val testObserver = reviewInteractor.review(review).test()

        testObserver.assertError(exception)
    }

    @Test
    fun review_errors_whenRuntimeException_inLogReview() {
        val exception = RuntimeException()
        stub_reviewRepository_getReviewHistory(Single.just(listOf(e1past)))
        stub_reviewRepository_logReview(Completable.error(exception))
        val review = Review.FreshReview(Reviewable(1), ReviewableFace.PICTURE, Date(), ReviewPerformance.FAILED)

        val testObserver = reviewInteractor.review(review).test()

        testObserver.assertError(exception)
    }

    // Stubs
    private fun stub_elementRepository_getReviewableIds(single: Single<List<Byte>>) {
        whenever(elementInteractor.getReviewableIds())
            .thenReturn(single)
    }

    private fun stub_reviewRepository_getReviewHistory(single: Single<List<Review>>) {
        whenever(reviewRepository.getReviewHistory())
            .thenReturn(single)
    }

    private fun stub_reviewRepository_logReview(completable: Completable) {
        whenever(reviewRepository.logReview(any()))
            .thenReturn(completable)
    }
}