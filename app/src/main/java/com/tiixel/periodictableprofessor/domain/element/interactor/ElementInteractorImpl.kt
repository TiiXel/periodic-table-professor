package com.tiixel.periodictableprofessor.domain.element.interactor

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.element.contract.ElementRepository
import com.tiixel.periodictableprofessor.domain.exception.AtomicNumberOutOfBoundsException
import io.reactivex.Single
import javax.inject.Inject

class ElementInteractorImpl @Inject constructor(
    private val repository: ElementRepository
) : ElementInteractor {

    override fun getElement(z: Byte): Single<Element> {
        return if (Element.verifyBounds(z)) {
            repository.getElements().map { it.first { it.atomicNumber == z } }
        } else {
            Single.error(AtomicNumberOutOfBoundsException(z))
        }
    }

    override fun getElements(): Single<List<Element>> {
        return repository.getElements()
    }
}