package com.tiixel.periodictableprofessor.data.mnemonic.local.sqlite

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.tiixel.periodictableprofessor.data.mnemonic.local.sqlite.entity.MnemonicPhraseEntity

const val MNEMONIC_PHRASE_TABLE_NAME = "mnemonic_phrases"

@Dao
abstract class MnemonicDao {

    @Query(value = "SELECT * FROM mnemonic_phrases WHERE atomic_number = :element AND language = :language")
    abstract fun getMnemonicPhrase(element: Byte, language: String): MnemonicPhraseEntity?

    @Query(value = "SELECT * FROM mnemonic_phrases WHERE language = :language")
    abstract fun getMnemonicPhrases(language: String): List<MnemonicPhraseEntity?>
}