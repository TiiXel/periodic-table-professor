package com.tiixel.periodictableprofessor.datarepository.review

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.tiixel.periodictableprofessor.datarepository.review.contract.ReviewLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.review.generic.GenericReview
import com.tiixel.periodictableprofessor.datarepository.review.mapper.ReviewMapper
import com.tiixel.periodictableprofessor.datarepository.review.mapper.ReviewMapperTest
import com.tiixel.periodictableprofessor.test.MockReview.e1later
import com.tiixel.periodictableprofessor.test.MockReview.e1past
import com.tiixel.periodictableprofessor.test.MockReview.e1soon
import com.tiixel.periodictableprofessor.test.MockReview.e2later
import com.tiixel.periodictableprofessor.test.MockReview.e3soon
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ReviewRepositoryImplTest {

    @Mock private lateinit var reviewDataSource: ReviewLocalDataSource
    private lateinit var reviewRepository: ReviewRepositoryImpl

    init {
        Assume.assumeTrue(ReviewMapperTest.util_toGeneric_maps())
        Assume.assumeTrue(ReviewMapperTest.util_toDomain_maps())
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        reviewRepository = ReviewRepositoryImpl(reviewDataSource)
    }

    // getReviewHistory()
    @Test
    fun getReviewHistory_returnsData() {
        val reviews = listOf(e1past, e1soon, e1later, e2later, e3soon)
        val generics = reviews.map(ReviewMapper::toGeneric)
        stub_reviewDataSource_getReviewHistory(Single.just(generics))

        val testObserver = reviewRepository.getReviewHistory().test()

        testObserver.assertValue(reviews)
    }

    @Test
    fun getReviewHistory_returnsData_empty() {
        stub_reviewDataSource_getReviewHistory(Single.just(listOf()))

        val testObserver = reviewRepository.getReviewHistory().test()

        testObserver.assertValue(listOf())
    }

    @Test
    fun getReviewHistory_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_reviewDataSource_getReviewHistory(Single.error(exception))

        val testObserver = reviewRepository.getReviewHistory().test()

        testObserver.assertError(exception)
    }

    // logReview()
    @Test
    fun logReview_completes() {
        stub_reviewDataSource_logReview(Completable.complete())

        val testObserver = reviewRepository.logReview(e1later).test()

        testObserver.assertComplete()
    }

    @Test
    fun logReview_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_reviewDataSource_logReview(Completable.error(exception))

        val testObserver = reviewRepository.logReview(e1later).test()

        testObserver.assertError(exception)
    }

    // Stubs
    private fun stub_reviewDataSource_getReviewHistory(single: Single<List<GenericReview>>) {
        whenever(reviewDataSource.getReviewHistory())
            .thenReturn(single)
    }

    private fun stub_reviewDataSource_logReview(completable: Completable) {
        whenever(reviewDataSource.logReview(any()))
            .thenReturn(completable)
    }
}