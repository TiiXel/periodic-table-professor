package com.tiixel.periodictableprofessor.datarepository.card

import com.tiixel.periodictableprofessor.datarepository.card.generic.StoredMnemonic
import io.reactivex.Single

interface MnemonicLocalDataSource {

    fun getMnemonic(element: Byte): Single<StoredMnemonic>

    fun getMnemonics(): Single<List<StoredMnemonic>>
}