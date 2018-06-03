package com.tiixel.periodictableprofessor.factory

import java.util.concurrent.ThreadLocalRandom

/**
 * Factory class for data instances
 */
class DataFactory {

    companion object Factory {

        fun randomUuid(): String {
            return java.util.UUID.randomUUID().toString()
        }

        fun randomInt(max: Int = 1000): Int {
            return ThreadLocalRandom.current().nextInt(1, max + 1)
        }

        fun randomLong(): Long {
            return randomInt().toLong()
        }

        fun randomBoolean(): Boolean {
            return Math.random() < 0.5
        }

        fun randomFloat(): Float {
            return randomInt().toFloat()
        }

        fun makeStringList(count: Int): List<String> {
            val items: MutableList<String> = mutableListOf()
            repeat(count) {
                items.add(randomUuid())
            }
            return items
        }

        fun makeAtomicNumber(not: Byte = 0): Byte {
            var atomicNumber: Byte = 0
            do {
                atomicNumber = ThreadLocalRandom.current().nextInt(1, 118 + 1).toByte()
            } while (atomicNumber == not)
            return atomicNumber
        }

        fun makeAtomicNumbers(): List<Byte> {
            return (1..118).toList().map { it.toByte() }
        }
    }
}