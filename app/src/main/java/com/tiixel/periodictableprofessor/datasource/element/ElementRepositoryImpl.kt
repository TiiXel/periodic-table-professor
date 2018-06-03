package com.tiixel.periodictableprofessor.datasource.element

import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.element.ElementRepository
import io.reactivex.Single
import javax.inject.Inject

class ElementRepositoryImpl @Inject constructor(private val localDataSource: ElementLocalDataSource) :
    ElementRepository {

    private var cachedElements: List<Element> = emptyList()

    override fun getElements(): Single<List<Element>> {
        if (cachedElements.isNotEmpty()) {
            return Single.just(cachedElements)
        } else {

            return localDataSource.getElements()
                .doOnSuccess { cachedElements = it }
        }
    }
}