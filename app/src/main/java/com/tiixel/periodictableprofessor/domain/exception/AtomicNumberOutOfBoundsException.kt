package com.tiixel.periodictableprofessor.domain.exception

class AtomicNumberOutOfBoundsException(number: Byte) : Exception(
    "Invalid element with atomic number: $number requested"
)