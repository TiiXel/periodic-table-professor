package com.tiixel.periodictableprofessor.data.mnemonic.local.asset

import android.content.Context
import javax.inject.Inject

class MnemonicPictureLoader @Inject constructor(private val context: Context) {

    fun getPicture(element: Byte) : ByteArray? {

        System.out.println("Accessing : $element")

        val regex = Regex("^${element}[a-z].*")
        val asset = context.resources.assets.list("pictures").firstOrNull { it.matches(regex) } ?: return null

        val reader = context.resources.assets.open("pictures/$asset")

        val data = reader.readBytes(1024)
        reader.close()

        return data
    }

}