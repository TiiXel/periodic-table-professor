package com.tiixel.periodictableprofessor.domain.element.interactor

import com.tiixel.periodictableprofessor.domain.element.Element
import com.tiixel.periodictableprofessor.domain.review.contract.ReviewableProvider
import io.reactivex.Single

interface ElementInteractor : ReviewableProvider {

    fun getElement(z: Byte): Single<Element>

    fun getElements(): Single<List<Element>>

}