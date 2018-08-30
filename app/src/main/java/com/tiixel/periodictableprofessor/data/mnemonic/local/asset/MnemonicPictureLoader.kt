package com.tiixel.periodictableprofessor.data.mnemonic.local.asset

import android.content.Context
import javax.inject.Inject

class MnemonicPictureLoader @Inject constructor(private val context: Context) {

    fun getPictures(): Map<Byte, ByteArray> {
        val regex = Regex("^[0-9]{1,3}[a-z].*")
        val filter = Regex("^([0-9]{1,3})")
        val assets = context.resources.assets.list("pictures").filter { it.matches(regex) }

        val pictures = emptyMap<Byte, ByteArray>().toMutableMap()

        assets.forEach {
            val reader = context.resources.assets.open("pictures/$it")
            val data = reader.readBytes(1024)
            reader.close()
            filter.find(it)?.value?.toByte()?.let {
                pictures.put(it, data)
            }
        }

        return pictures
    }
}