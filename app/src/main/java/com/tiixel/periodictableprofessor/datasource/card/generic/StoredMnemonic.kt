package com.tiixel.periodictableprofessor.datasource.card.generic

import com.tiixel.periodictableprofessor.domain.Card

data class StoredMnemonic(
    val element: Byte,
    val mnemonicPhrase: String?,
    val mnemonicPicture: ByteArray?
) {

    companion object {

        fun toDomain(mnemonic: StoredMnemonic): Card.Mnemonic? {
            if (mnemonic.mnemonicPhrase == null || mnemonic.mnemonicPicture == null) {
                return null
            }
            return Card.Mnemonic(
                    phrase = mnemonic.mnemonicPhrase,
                    picture = mnemonic.mnemonicPicture
                )
        }
    }
}