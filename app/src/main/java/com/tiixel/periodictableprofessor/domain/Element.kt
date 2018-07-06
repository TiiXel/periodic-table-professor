package com.tiixel.periodictableprofessor.domain

data class Element(
    val abundanceCrust: Quantity?,
    val abundanceSea: Quantity?,
    val atomicNumber: Byte,
    val atomicRadius: Quantity?,
    val atomicWeight: Quantity?,
    val description: String?,
    val discoverers: String?,
    val discoveryLocation: String?,
    val discoveryYear: Quantity?,
    val electronicConfiguration: String,
    val enPauling: Quantity?,
    val isRadioactive: Boolean,
    val name: String,
    val nameOrigin: String?,
    val sources: String?,
    val symbol: String,
    val uses: String?,
    val vdwRadius: Quantity?,
    val mnemonicPhrase: String?,
    val mnemonicPicture: ByteArray?,
    val mnemonicUserNote: String?
) {

    val tableColumn: Byte = tableColumnOf(atomicNumber)

    val tableRow: Byte = tableRowOf(atomicNumber)

    companion object {

        fun verifyBounds(number: Byte): Boolean {
            return !(number <= 0 || number > 118)
        }

        fun tableColumnOf(atomicNumber: Byte): Byte {
            var colOffset = 0

            if (atomicNumber > 1) colOffset += 16
            if (atomicNumber > 4) colOffset += 10
            if (atomicNumber > 12) colOffset += 10
            if (atomicNumber > 70) colOffset -= 14
            if (atomicNumber > 102) colOffset -= 14

            return when (atomicNumber) {
                in 57..70 -> (atomicNumber - 1 - 57) % 14 + 3
                in 89..102 -> (atomicNumber - 1 - 89) % 14 + 3
                else -> (atomicNumber - 1 + colOffset) % 18
            }.toByte()
        }

        fun tableRowOf(atomicNumber: Byte): Byte {
            return when (atomicNumber) {
                in 57..70 -> 8
                in 89..102 -> 9
                in 1..2 -> 1
                in 2..10 -> 2
                in 11..18 -> 3
                in 19..36 -> 4
                in 37..54 -> 5
                in 55..86 -> 6
                in 87..118 -> 7
                else -> 0
            }.minus(1).toByte()
        }
    }
}