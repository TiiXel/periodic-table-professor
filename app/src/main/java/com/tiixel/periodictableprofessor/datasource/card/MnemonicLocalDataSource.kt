package com.tiixel.periodictableprofessor.datasource.card

import com.tiixel.periodictableprofessor.datasource.card.generic.StoredMnemonic
import io.reactivex.Single

interface MnemonicLocalDataSource {

    fun getMnemonic(element: Byte): Single<StoredMnemonic>

    fun getMnemonics(): Single<List<StoredMnemonic>>
}