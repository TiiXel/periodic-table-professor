package com.tiixel.periodictableprofessor.domain.element.interactor

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.element.contract.ElementRepository
import com.tiixel.periodictableprofessor.domain.exception.AtomicNumberOutOfBoundsException
import io.reactivex.Single
import javax.inject.Inject

class ElementInteractorImpl @Inject constructor(private val repository: ElementRepository) :
    ElementInteractor {

    override fun getElement(z: Byte): Single<Element> {
        if (Element.verifyBounds(z)) {
            return repository.getElements()
                .map { it.first { it.atomicNumber == z } }
        } else {
            return Single.error(AtomicNumberOutOfBoundsException(z))
        }
    }

    override fun getElements(): Single<List<Element>> {
        return repository.getElements()
    }
}