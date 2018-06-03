package com.tiixel.periodictableprofessor.domain.element

import com.tiixel.periodictableprofessor.domain.Element
import io.reactivex.Single
import javax.inject.Inject

class ElementInteractorImpl @Inject constructor(private val repository: ElementRepository) : ElementInteractor {

    override fun getElement(z: Byte): Single<Element> {
        Element.verifyBounds(z)
        return repository.getElements()
            .map { it.first { it.atomicNumber == z } }
    }

    override fun getElements(): Single<List<Element>> {
        return repository.getElements()
    }
}