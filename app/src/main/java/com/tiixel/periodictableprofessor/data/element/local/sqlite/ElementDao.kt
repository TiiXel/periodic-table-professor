package com.tiixel.periodictableprofessor.data.element.local.sqlite

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.tiixel.periodictableprofessor.data.element.local.sqlite.entity.ElementEntity

const val ELEMENTS_TABLE_NAME = "elements"

const val ELEMENTS_LANG_TABLE_NAME = "elements_lang"

@Dao
abstract class ElementDao {

    @Query(value = "SELECT elements.*, elements_lang.* FROM elements LEFT JOIN elements_lang ON elements_lang.atomic_number = elements.atomic_number WHERE elements_lang.language = :language AND elements.atomic_number=:atomicNumber")
    abstract fun getElement(atomicNumber: Int, language: String): ElementEntity

    @Query(value = "SELECT elements.*, elements_lang.* FROM elements LEFT JOIN elements_lang ON elements_lang.atomic_number = elements.atomic_number WHERE elements_lang.language = :language")
    abstract fun getElements(language: String): List<ElementEntity>
}