package com.tiixel.periodictableprofessor.data.mnemonic.local

import com.google.gson.Gson
import com.tiixel.periodictableprofessor.data.JsonAssets
import com.tiixel.periodictableprofessor.data.mnemonic.local.asset.MnemonicPictureLoader
import com.tiixel.periodictableprofessor.data.mnemonic.local.gson.entity.MnemonicPhraseEntity
import com.tiixel.periodictableprofessor.datarepository.element.contract.MnemonicLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericMnemonic
import io.reactivex.Single
import javax.inject.Inject

class MnemonicMixedDataSource @Inject constructor(
    private val assets: MnemonicPictureLoader,
    private val jsonAssets: JsonAssets
) : MnemonicLocalDataSource {

    override fun getMnemonics(): Single<List<GenericMnemonic>> {
        return Single.defer {
            val pictures = assets.getPictures()
            val gson = Gson()
            val phrasesJson = jsonAssets.mnemonicPhraseJson()
            val phrases =
                gson.fromJson<Array<MnemonicPhraseEntity>>(phrasesJson, Array<MnemonicPhraseEntity>::class.java)
                    .map { it.atomic_number to it }.toMap()

            val mnemonics = emptyMap<Byte, GenericMnemonic>().toMutableMap()

            pictures.forEach {
                mnemonics.put(it.key,
                    GenericMnemonic(
                        it.key,
                        null,
                        it.value
                    )
                )
            }

            phrases.forEach {
                val m = mnemonics[it.key] ?: GenericMnemonic(
                    it.key,
                    null,
                    null
                )
                mnemonics.put(it.key, m.copy(mnemonicPhrase = it.value.phrase))
            }

            Single.just(mnemonics.values.toList())
        }
    }

    override fun getMnemonic(element: Byte): Single<GenericMnemonic> {
        return Single.defer {
            val picture = assets.getPicture(element)

            val gson = Gson()

            val phrasesJson = jsonAssets.mnemonicPhraseJson()
            val phrases =
                gson.fromJson<Array<MnemonicPhraseEntity>>(phrasesJson, Array<MnemonicPhraseEntity>::class.java)
            val phrase = phrases.first { it.atomic_number == element }

            Single.just(
                GenericMnemonic(
                    element = element,
                    mnemonicPicture = picture,
                    mnemonicPhrase = phrase.phrase
                )
            )
        }
    }
}