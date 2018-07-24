package com.tiixel.periodictableprofessor.datarepository.element

import com.nhaarman.mockito_kotlin.whenever
import com.tiixel.periodictableprofessor.datarepository.element.contract.ElementLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.contract.MnemonicLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.contract.UserNoteLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.mapper.ElementMapper
import com.tiixel.periodictableprofessor.datarepository.element.mapper.ElementMapperTest
import com.tiixel.periodictableprofessor.domain.element.Element
import com.tiixel.periodictableprofessor.test.MockElement.e1
import com.tiixel.periodictableprofessor.test.MockElement.e2
import com.tiixel.periodictableprofessor.test.MockElement.e3
import io.reactivex.Single
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ElementRepositoryImplTest {

    @Mock private lateinit var elementLocalDataSource: ElementLocalDataSource
    @Mock private lateinit var mnemonicLocalDataSource: MnemonicLocalDataSource
    @Mock private lateinit var userNoteLocalDataSource: UserNoteLocalDataSource
    private lateinit var elementRepository: ElementRepositoryImpl

    init {
        Assume.assumeTrue(ElementMapperTest.util_toGeneric_maps())
        Assume.assumeTrue(ElementMapperTest.util_toDomain_maps())
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        elementRepository = ElementRepositoryImpl(
            elementLocalDataSource,
            mnemonicLocalDataSource,
            userNoteLocalDataSource
        )
    }

    // getElements()
    @Test
    fun getElements_returnsData() {
        stub_elementLocalDataSource_getElement(Single.just(listOf(e1, e2, e3)))
        stub_mnemonicLocalDataSource_getMnemonics(Single.just(listOf(e1, e2, e3)))
        stub_userNoteLocalDataSource_getUserNotes(Single.just(listOf(e1, e2, e3)))

        val testObserver = elementRepository.getElements().test()

        testObserver.assertValue(listOf(e1, e2, e3))
    }

    @Test
    fun getElements_returnsData_whenMissingMnemonics() {
        stub_elementLocalDataSource_getElement(Single.just(listOf(e1, e2, e3)))
        stub_mnemonicLocalDataSource_getMnemonics(Single.just(listOf(e1, e3)))
        stub_userNoteLocalDataSource_getUserNotes(Single.just(listOf(e1, e2, e3)))

        val testObserver = elementRepository.getElements().test()

        testObserver.assertValue(listOf(e1, e2.copy(mnemonicPhrase = null, mnemonicPicture = null), e3))
    }

    @Test
    fun getElements_returnsData_whenMissingUserNotes() {
        stub_elementLocalDataSource_getElement(Single.just(listOf(e1, e2, e3)))
        stub_mnemonicLocalDataSource_getMnemonics(Single.just(listOf(e1, e2, e3)))
        stub_userNoteLocalDataSource_getUserNotes(Single.just(listOf(e1, e2)))

        val testObserver = elementRepository.getElements().test()

        testObserver.assertValue(listOf(e1, e2, e3.copy(mnemonicUserNote = null)))
    }

    @Test
    fun getElements_errors_whenRuntimeException_inGetElements() {
        val exception = RuntimeException()
        stub_elementLocalDataSource_getElement(Single.error(exception))
        stub_mnemonicLocalDataSource_getMnemonics(Single.just(listOf(e1, e3)))
        stub_userNoteLocalDataSource_getUserNotes(Single.just(listOf(e1, e2, e3)))

        val testObserver = elementRepository.getElements().test()

        testObserver.assertError(exception)
    }

    @Test
    fun getElements_errors_whenRuntimeException_inGetMnemonics() {
        val exception = RuntimeException()
        stub_elementLocalDataSource_getElement(Single.just(listOf(e1, e2, e3)))
        stub_mnemonicLocalDataSource_getMnemonics(Single.error(exception))
        stub_userNoteLocalDataSource_getUserNotes(Single.just(listOf(e1, e2, e3)))

        val testObserver = elementRepository.getElements().test()

        testObserver.assertError(exception)
    }

    @Test
    fun getElements_errors_whenRuntimeException_inGetUserNotes() {
        val exception = RuntimeException()
        stub_elementLocalDataSource_getElement(Single.just(listOf(e1, e2, e3)))
        stub_mnemonicLocalDataSource_getMnemonics(Single.just(listOf(e1, e3)))
        stub_userNoteLocalDataSource_getUserNotes(Single.error(exception))

        val testObserver = elementRepository.getElements().test()

        testObserver.assertError(exception)
    }

    @Test
    fun getElements_cachesAfterFirstRequest() {
        stub_elementLocalDataSource_getElement(Single.just(listOf(e1, e2)))
        stub_mnemonicLocalDataSource_getMnemonics(Single.just(listOf(e1, e2)))
        stub_userNoteLocalDataSource_getUserNotes(Single.just(listOf(e1, e2)))

        val testObserver1 = elementRepository.getElements().test()

        stub_elementLocalDataSource_getElement(Single.just(listOf(e1, e2, e3)))
        stub_mnemonicLocalDataSource_getMnemonics(Single.just(listOf(e1, e2, e3)))
        stub_userNoteLocalDataSource_getUserNotes(Single.just(listOf(e1, e2, e3)))

        val testObserver2 = elementRepository.getElements().test()

        testObserver1.assertValue(listOf(e1, e2))
        testObserver2.assertValue(listOf(e1, e2))
    }

    // Stubs
    private fun stub_elementLocalDataSource_getElement(single: Single<List<Element>>) {
        whenever(elementLocalDataSource.getElements())
            .thenReturn(single.map { it.map(ElementMapper::toGenericElement) })
    }

    private fun stub_mnemonicLocalDataSource_getMnemonics(single: Single<List<Element>>) {
        whenever(mnemonicLocalDataSource.getMnemonics())
            .thenReturn(single.map { it.map(ElementMapper::toGenericMnemonic) })
    }

    private fun stub_userNoteLocalDataSource_getUserNotes(single: Single<List<Element>>) {
        whenever(userNoteLocalDataSource.getUserNotes())
            .thenReturn(single.map { it.map(ElementMapper::toGenericUserNote) })
    }
}