package com.tiixel.periodictableprofessor.domain.element.interactor

import com.tiixel.periodictableprofessor.domain.element.Element
import com.tiixel.periodictableprofessor.domain.element.contract.ElementRepository
import com.tiixel.periodictableprofessor.domain.exception.AtomicNumberOutOfBoundsException
import io.reactivex.Single
import javax.inject.Inject

class ElementInteractorImpl @Inject constructor(
    private val repository: ElementRepository
) : ElementInteractor {

    override fun getReviewableIds(): Single<List<Byte>> {
        return repository.getElements()
            .map { it.filter { it.mnemonicPhrase != null && it.mnemonicPicture != null } }
            .map { it.map { it.atomicNumber } }
    }

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