package com.tiixel.periodictableprofessor.data.element.local

import com.google.gson.Gson
import com.tiixel.periodictableprofessor.data.JsonAssets
import com.tiixel.periodictableprofessor.data.element.local.gson.entity.ElementEntity
import com.tiixel.periodictableprofessor.data.element.local.gson.entity.ElementLangEntity
import com.tiixel.periodictableprofessor.data.element.local.gson.mapper.ElementMapper
import com.tiixel.periodictableprofessor.datarepository.element.contract.ElementLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericElement
import io.reactivex.Single
import javax.inject.Inject

class ElementGsonDataSource @Inject constructor(
    private val jsonAssets: JsonAssets
) : ElementLocalDataSource {

    override fun getElement(z: Int): Single<GenericElement> {
        return getElements()
            .map { it.first { it.atomicNumber == z.toByte() } }
    }

    override fun getElements(): Single<List<GenericElement>> {
        return Single.defer {
            val gson = Gson()

            val elementsJson = jsonAssets.elementJson()
            val elements = gson.fromJson<Array<ElementEntity>>(elementsJson, Array<ElementEntity>::class.java)

            val elementsLangJson = jsonAssets.elementLangJson()
            val elementsLang =
                gson.fromJson<Array<ElementLangEntity>>(elementsLangJson, Array<ElementLangEntity>::class.java)

            val data = elements.sortedBy { it.atomic_number }.zip(elementsLang.sortedBy { it.atomic_number })

            Single.just(data).map { it.map { ElementMapper.toGeneric(it.first, it.second) } }
        }
    }
}