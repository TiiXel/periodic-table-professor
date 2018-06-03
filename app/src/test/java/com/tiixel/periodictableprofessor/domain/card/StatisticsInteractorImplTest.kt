package com.tiixel.periodictableprofessor.domain.card

import com.nhaarman.mockito_kotlin.whenever
import com.tiixel.periodictableprofessor.domain.ReviewData
import com.tiixel.periodictableprofessor.domain.StatisticsInteractorImpl
import com.tiixel.periodictableprofessor.factory.ReviewDataFactory
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.text.SimpleDateFormat

class StatisticsInteractorImplTest {

    @Mock private lateinit var cardRepository: CardRepository

    private lateinit var statisticsInteractor: StatisticsInteractorImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        statisticsInteractor = StatisticsInteractorImpl(cardRepository)
    }

    @Test
    fun getReviewCountPerDayHistory_errors() {
        val exception = RuntimeException()
        stub_cardRepository_getReviewLog(Single.error(exception))

        val testObserver = statisticsInteractor.getReviewCountPerDayHistory().test()

        testObserver.assertError(exception)
    }

    @Test
    fun getReviewCountPerDayHistory_returnsData() {
        val log = ReviewDataFactory.makePerfectReviewHistory(11)
        stub_cardRepository_getReviewLog(Single.just(log))

        val testObserver = statisticsInteractor.getReviewCountPerDayHistory().test()

        val formatter = SimpleDateFormat("yyyy MM dd HH:mm:ss z")
        val expectedDates = listOf(formatter.parse("1970 01 01 00:00:00 UTC"),
            formatter.parse("1970 01 02 00:00:00 UTC"),
            formatter.parse("1970 01 06 00:00:00 UTC"),
            formatter.parse("1970 01 16 00:00:00 UTC"),
            formatter.parse("1970 02 17 00:00:00 UTC"),
            formatter.parse("1970 05 22 00:00:00 UTC"))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        expectedDates.forEachIndexed { index, date ->
            testObserver.assertValue {
                it.containsKey(date)
            }
            testObserver.assertValue {
                it[date] == Pair(if (index == 0) 6 else 1, 0)
            }
        }
    }

    private fun stub_cardRepository_getReviewLog(single: Single<List<ReviewData>>) {
        whenever(cardRepository.getReviewLog())
            .thenReturn(single)
    }
}