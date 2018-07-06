package com.tiixel.periodictableprofessor.datarepository.card

import com.tiixel.periodictableprofessor.datarepository.card.generic.GenericMnemonic
import io.reactivex.Single

interface MnemonicLocalDataSource {

    fun getMnemonic(element: Byte): Single<GenericMnemonic>

    fun getMnemonics(): Single<List<GenericMnemonic>>
}