package com.tiixel.periodictableprofessor.data.element.local.sqlite.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import com.tiixel.periodictableprofessor.data.element.local.sqlite.ELEMENTS_LANG_TABLE_NAME

@Entity(
    tableName = ELEMENTS_LANG_TABLE_NAME,
    foreignKeys = [
        (ForeignKey(
            entity = ElementEntity::class,
            parentColumns = arrayOf("atomic_number"),
            childColumns = arrayOf("atomic_number")
        ))],
    primaryKeys = ["atomic_number", "language"]
)
data class ElementLangEntity(
    val atomic_number: Int,
    val language: String,
    val name: String,
    val name_origin: String
)