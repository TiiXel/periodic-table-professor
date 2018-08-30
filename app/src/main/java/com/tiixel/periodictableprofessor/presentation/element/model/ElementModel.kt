package com.tiixel.periodictableprofessor.presentation.element.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tiixel.periodictableprofessor.domain.element.Element

data class ElementModel(
    val abundanceCrust: String?,
    val abundanceSea: String?,
    val atomicNumber: String,
    val atomicRadius: String?,
    val atomicWeight: String?,
    val description: String?,
    val discoverers: String?,
    val discoveryLocation: String?,
    val discoveryYear: String?,
    val electronicConfiguration: String,
    val enPauling: String?,
    val isRadioactive: Boolean,
    val name: String,
    val nameOrigin: String?,
    val sources: String?,
    val symbol: String,
    val uses: String?,
    val vdwRadius: String?,
    val mnemonicPhrase: String?,
    val mnemonicPicture: Bitmap?
) {

    companion object {

        fun fromDomain(element: Element): ElementModel {
            return ElementModel(
                abundanceCrust = element.abundanceCrust?.toString(),
                abundanceSea = element.abundanceSea?.toString(),
                atomicNumber = element.atomicNumber.toString(),
                atomicRadius = element.atomicRadius?.toString(),
                atomicWeight = element.atomicWeight?.toString(),
                description = element.description,
                discoverers = element.discoverers,
                discoveryLocation = element.discoveryLocation,
                discoveryYear = element.discoveryYear?.toString(),
                electronicConfiguration = element.electronicConfiguration,
                enPauling = element.enPauling?.toString(),
                isRadioactive = element.isRadioactive,
                name = element.name,
                nameOrigin = element.nameOrigin,
                sources = element.sources,
                symbol = element.symbol,
                uses = element.uses,
                vdwRadius = element.vdwRadius?.toString(),
                mnemonicPhrase = element.mnemonicPhrase,
                mnemonicPicture = element.mnemonicPicture?.let {
                    BitmapFactory.decodeByteArray(
                        it,
                        0,
                        it.size
                    )
                }
            )
        }
    }
}