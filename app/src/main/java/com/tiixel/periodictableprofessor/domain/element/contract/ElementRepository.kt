package com.tiixel.periodictableprofessor.domain.element.contract

import com.tiixel.periodictableprofessor.domain.Element
import io.reactivex.Single

interface ElementRepository {

    fun getElements(): Single<List<Element>>

}