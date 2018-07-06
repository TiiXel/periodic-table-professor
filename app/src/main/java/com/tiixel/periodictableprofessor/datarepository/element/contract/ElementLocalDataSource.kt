package com.tiixel.periodictableprofessor.datarepository.element.contract

import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericElement
import io.reactivex.Single

interface ElementLocalDataSource {

    fun getElement(z: Int): Single<GenericElement>

    fun getElements(): Single<List<GenericElement>>
}