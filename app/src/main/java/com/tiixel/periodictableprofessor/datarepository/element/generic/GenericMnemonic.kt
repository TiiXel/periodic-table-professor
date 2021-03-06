package com.tiixel.periodictableprofessor.datarepository.element.generic


data class GenericMnemonic(
    val element: Byte,
    val mnemonicPhrase: String?,
    val mnemonicPicture: ByteArray?
)