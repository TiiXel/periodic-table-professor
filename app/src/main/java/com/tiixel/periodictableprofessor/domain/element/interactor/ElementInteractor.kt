package com.tiixel.periodictableprofessor.domain.element.interactor

import com.tiixel.periodictableprofessor.domain.element.Element
import io.reactivex.Single

interface ElementInteractor {

    fun getElement(z: Byte): Single<Element>

    fun getElements(): Single<List<Element>>

}