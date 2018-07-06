package com.tiixel.periodictableprofessor.datarepository.element

import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericMnemonic
import io.reactivex.Single

interface MnemonicLocalDataSource {

    fun getMnemonic(element: Byte): Single<GenericMnemonic>

    fun getMnemonics(): Single<List<GenericMnemonic>>
}