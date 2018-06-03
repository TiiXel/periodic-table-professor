package com.tiixel.periodictableprofessor.domain.element

import com.tiixel.periodictableprofessor.domain.Element
import io.reactivex.Single

interface ElementInteractor {

    fun getElement(z: Byte): Single<Element>

    fun getElements(): Single<List<Element>>

}