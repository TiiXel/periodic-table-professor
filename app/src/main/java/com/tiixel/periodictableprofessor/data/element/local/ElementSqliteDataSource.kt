package com.tiixel.periodictableprofessor.data.element.local

import com.tiixel.periodictableprofessor.data.SqliteDatabase
import com.tiixel.periodictableprofessor.data.element.local.sqlite.entity.ElementEntity
import com.tiixel.periodictableprofessor.datasource.element.ElementLocalDataSource
import com.tiixel.periodictableprofessor.domain.Element
import io.reactivex.Single
import javax.inject.Inject

class ElementSqliteDataSource @Inject constructor(
    private val database: SqliteDatabase
) : ElementLocalDataSource {

    override fun getElement(z: Int): Single<Element> {
        return Single.defer {
            Single.just(ElementEntity.toDomain(database.elementDao().getElement(z, "en")))
        }
    }

    override fun getElements(): Single<List<Element>> {
        return Single.defer { Single.just(database.elementDao().getElements("en").map { ElementEntity.toDomain(it) }) }
    }
}