package com.tiixel.periodictableprofessor.presentation.study.model

import android.graphics.Bitmap

data class ElementModel(
    val number: String,
    val symbol: String,
    val name: String,
    val column: Byte,
    val row: Byte,
    val mnemonicPicture: Bitmap?,
    val mnemonicPhrase: String?,
    val userNote: String?
)