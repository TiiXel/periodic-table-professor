package com.tiixel.periodictableprofessor.datasource.card.generic


data class StoredMnemonic(
    val element: Byte,
    val mnemonicPhrase: String?,
    val mnemonicPicture: ByteArray?
)