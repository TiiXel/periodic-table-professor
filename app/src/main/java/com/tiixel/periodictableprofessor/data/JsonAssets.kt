package com.tiixel.periodictableprofessor.data

import android.app.Application
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject

class JsonAssets @Inject constructor(
    private val application: Application
) {

    fun elementJson() = loadJsonFromAsset("json/elements.json")

    fun elementLangJson() = loadJsonFromAsset("json/elements_lang_en.json")

    fun mnemonicPhraseJson() = loadJsonFromAsset("json/mnemonic_phrases_en.json")

    private fun loadJsonFromAsset(fileName: String): String? {

        var json: String? = null

        try {
            val input = application.assets.open(fileName)
            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}