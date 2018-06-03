package com.tiixel.periodictableprofessor.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.tiixel.periodictableprofessor.data.element.local.sqlite.ElementDao
import com.tiixel.periodictableprofessor.data.element.local.sqlite.entity.ElementEntity
import com.tiixel.periodictableprofessor.data.element.local.sqlite.entity.ElementLangEntity
import com.tiixel.periodictableprofessor.data.mnemonic.local.sqlite.MnemonicDao
import com.tiixel.periodictableprofessor.data.mnemonic.local.sqlite.entity.MnemonicPhraseEntity

@Database(
    entities = arrayOf(
        ElementEntity::class,
        ElementLangEntity::class,

        MnemonicPhraseEntity::class
    ),
    version = 1
)
abstract class SqliteDatabase : RoomDatabase() {

    abstract fun elementDao(): ElementDao

    abstract fun mnemonicDao(): MnemonicDao
}