package com.tiixel.periodictableprofessor.domain.element

import com.nhaarman.mockito_kotlin.whenever
import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.factory.ElementFactory.makeElements
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ElementInteractorImplTest {

    @Mock private lateinit var mockRepository: ElementRepository

    private lateinit var elementUseCase: ElementInteractorImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        elementUseCase = ElementInteractorImpl(mockRepository)
    }

    @Test
    fun getElements_completes() {
        val elements = makeElements()
        stub_repository_getElements(Single.just(elements))

        val testObserver = elementUseCase.getElements().test()

        testObserver.assertComplete()
    }

    @Test
    fun getElements_returnsData_whenElementsAvailable() {
        val elements = makeElements()
        stub_repository_getElements(Single.just(elements))

        val testObserver = elementUseCase.getElements().test()

        testObserver.assertValue(elements)
    }

    @Test
    fun getElements_errors_whenElementsNotAvailable() {
        val exception = RuntimeException()
        stub_repository_getElements(Single.error(exception))

        val testObserver = elementUseCase.getElements().test()

        testObserver.assertError(exception)
    }

    private fun stub_repository_getElements(single: Single<List<Element>>) {
        whenever(mockRepository.getElements())
            .thenReturn(single)
    }

}