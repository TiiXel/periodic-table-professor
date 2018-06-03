package com.tiixel.periodictableprofessor.datasource.element

import com.tiixel.periodictableprofessor.domain.Element
import io.reactivex.Single

interface ElementLocalDataSource {

    fun getElement(z: Int): Single<Element>

    fun getElements(): Single<List<Element>>
}