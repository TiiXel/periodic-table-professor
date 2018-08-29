package com.tiixel.periodictableprofessor.domain.element

import com.nhaarman.mockito_kotlin.whenever
import com.tiixel.periodictableprofessor.domain.element.contract.ElementRepository
import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractorImpl
import com.tiixel.periodictableprofessor.domain.exception.AtomicNumberOutOfBoundsException
import com.tiixel.periodictableprofessor.test.MockElement.e1
import com.tiixel.periodictableprofessor.test.MockElement.e2
import com.tiixel.periodictableprofessor.test.MockElement.e3
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ElementInteractorImplTest {

    @Mock private lateinit var elementRepository: ElementRepository
    private lateinit var elementInteractor: ElementInteractorImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        elementInteractor = ElementInteractorImpl(elementRepository)
    }

    // getReviewableIds()
    @Test
    fun getReviewableIds_returnsData() {
        val e1n = e1.copy(mnemonicPhrase = null)
        val e3n = e3.copy(mnemonicPicture = null)
        stub_elementRepository_getElements(Single.just(listOf(e1n, e2, e3n)))

        val testObserver = elementInteractor.getReviewableIds().test()

        testObserver.assertValue(listOf<Byte>(2))
    }

    @Test
    fun getReviewableIds_returnsData_empty() {
        val e1n = e1.copy(mnemonicPhrase = null)
        val e3n = e3.copy(mnemonicPicture = null)
        stub_elementRepository_getElements(Single.just(listOf(e1n, e3n)))

        val testObserver = elementInteractor.getReviewableIds().test()

        testObserver.assertValue(listOf())
    }

    @Test
    fun getReviewableIds_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_elementRepository_getElements(Single.error(exception))

        val testObserver = elementInteractor.getReviewableIds().test()

        testObserver.assertError(exception)
    }

    // getElement()
    @Test
    fun getElements_returnsData() {
        val elements = listOf(e1, e2, e3)
        stub_elementRepository_getElements(Single.just(elements))

        val testObserver = elementInteractor.getElements().test()

        testObserver.assertValue(elements)
    }

    @Test
    fun getElements_returnsData_empty() {
        stub_elementRepository_getElements(Single.just(listOf()))

        val testObserver = elementInteractor.getElements().test()

        testObserver.assertValue(listOf())
    }

    @Test
    fun getElements_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_elementRepository_getElements(Single.error(exception))

        val testObserver = elementInteractor.getElements().test()

        testObserver.assertError(exception)
    }

    // getElement()
    @Test
    fun getElement_returnsData() {
        stub_elementRepository_getElements(Single.just(listOf(e1, e2, e3)))

        val testObserver = elementInteractor.getElement(1).test()

        testObserver.assertValue(e1)
    }

    @Test
    fun getElement_errors_whenAtomicNumberOutOfBounds() {
        stub_elementRepository_getElements(Single.just(listOf(e1, e2, e3)))

        val testObserver = elementInteractor.getElement(127).test()

        testObserver.assertError { it is AtomicNumberOutOfBoundsException }
    }

    @Test
    fun getElement_errors_whenNoSuchElement() {
        stub_elementRepository_getElements(Single.just(listOf(e1, e2, e3)))

        val testObserver = elementInteractor.getElement(4).test()

        testObserver.assertError { it is NoSuchElementException }
    }

    @Test
    fun getElement_errors_whenRuntimeException() {
        val exception = RuntimeException()
        stub_elementRepository_getElements(Single.error(exception))

        val testObserver = elementInteractor.getElement(1).test()

        testObserver.assertError(exception)
    }

    private fun stub_elementRepository_getElements(single: Single<List<Element>>) {
        whenever(elementRepository.getElements())
            .thenReturn(single)
    }
}