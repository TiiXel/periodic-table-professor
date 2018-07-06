package com.tiixel.periodictableprofessor.datasource.element

import com.tiixel.periodictableprofessor.datasource.element.generic.StoredElement
import io.reactivex.Single

interface ElementLocalDataSource {

    fun getElement(z: Int): Single<StoredElement>

    fun getElements(): Single<List<StoredElement>>
}