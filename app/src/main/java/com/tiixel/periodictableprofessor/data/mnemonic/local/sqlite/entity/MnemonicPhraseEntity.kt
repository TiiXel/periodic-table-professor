package com.tiixel.periodictableprofessor.data.mnemonic.local.sqlite.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tiixel.periodictableprofessor.data.mnemonic.local.sqlite.MNEMONIC_PHRASE_TABLE_NAME

@Entity(tableName = MNEMONIC_PHRASE_TABLE_NAME)
data class MnemonicPhraseEntity(
    @PrimaryKey
    val atomic_number: Byte,
    val language: String,
    val phrase: String
)