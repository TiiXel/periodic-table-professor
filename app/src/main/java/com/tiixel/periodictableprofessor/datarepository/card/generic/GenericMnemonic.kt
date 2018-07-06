package com.tiixel.periodictableprofessor.datarepository.card.generic


data class GenericMnemonic(
    val element: Byte,
    val mnemonicPhrase: String?,
    val mnemonicPicture: ByteArray?
)