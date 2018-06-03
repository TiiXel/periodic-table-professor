package com.tiixel.periodictableprofessor.data.mnemonic.local

import com.tiixel.periodictableprofessor.data.SqliteDatabase
import com.tiixel.periodictableprofessor.data.mnemonic.local.asset.MnemonicPictureLoader
import com.tiixel.periodictableprofessor.datasource.card.MnemonicLocalDataSource
import com.tiixel.periodictableprofessor.datasource.card.generic.StoredMnemonic
import io.reactivex.Single
import javax.inject.Inject

class MnemonicMixedDataSource @Inject constructor(
    private val database: SqliteDatabase, private val assets: MnemonicPictureLoader
) : MnemonicLocalDataSource {

    override fun getMnemonic(element: Byte): Single<StoredMnemonic> {
        return Single.defer {
            val picture = assets.getPicture(element)
            val phrase = database.mnemonicDao().getMnemonicPhrase(element, "en")

            Single.just(
                StoredMnemonic(
                    element = element,
                    mnemonicPicture = picture,
                    mnemonicPhrase = phrase?.phrase
                )
            )
        }
    }
}