package com.tiixel.periodictableprofessor.datarepository.card.generic


data class StoredMnemonic(
    val element: Byte,
    val mnemonicPhrase: String?,
    val mnemonicPicture: ByteArray?
)