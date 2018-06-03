package com.tiixel.periodictableprofessor.ui.elementlist.model

import com.tiixel.periodictableprofessor.presentation.elementlist.model.ElementModel

data class ElementCellModel(
    val number: Byte,
    val symbol: String,
    val data: String,
    val column: Byte,
    val row: Byte,
    val backgroundColor: String
) {

    companion object Mapper {

        fun listFromPresentation(models: List<ElementModel>): List<ElementCellModel> {
            return models.mapIndexed { index, model ->
                ElementCellModel(
                    number = model.atomicNumber,
                    symbol = model.symbol,
                    data = model.data ?: "",
                    column = model.column,
                    row = model.row,
                    // TODO: Scale values so colors are rightly linear
                    backgroundColor = colorArray[colorArray.size * index / models.size]
                )
            }
        }

        val colorArray = arrayListOf(
            "#ffebee", // Color used if value is null
            "#ffcdd2",
            "#fec8cd",
            "#fdc3c7",
            "#fcbfc2",
            "#fbbabd",
            "#fab6b8",
            "#f9b0b3",
            "#f8acae",
            "#f6a7a9",
            "#f5a2a3",
            "#f39e9e",
            "#f2999a",
            "#f19495",
            "#ef9090",
            "#ee8b8b",
            "#ec8786",
            "#ea8281",
            "#e87d7c",
            "#e77877",
            "#e57373"
        )
    }
}