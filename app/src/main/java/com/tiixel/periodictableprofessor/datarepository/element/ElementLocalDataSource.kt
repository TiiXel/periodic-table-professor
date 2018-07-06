package com.tiixel.periodictableprofessor.datarepository.element

import com.tiixel.periodictableprofessor.datarepository.element.generic.StoredElement
import io.reactivex.Single

interface ElementLocalDataSource {

    fun getElement(z: Int): Single<StoredElement>

    fun getElements(): Single<List<StoredElement>>
}