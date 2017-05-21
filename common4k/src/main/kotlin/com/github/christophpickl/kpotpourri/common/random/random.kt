package com.github.christophpickl.kpotpourri.common.random

import java.lang.IllegalArgumentException


/**
 * Random generator as an interface in order to be testable.
 */
interface RandX {
    /**
     * Generate a random number based on the given range (inclusive), optionally specifying an except value.
     */
    fun randomBetween(from: Int, to: Int, except: Int? = null): Int

    fun <T> randomOf(items: List<T>, except: T?): T
//    fun <T> randomElementsExcept(items: List<T>, randomElementsCount: Int, except: T): List<T>
}

/**
 * Default implementation of RandX.
 */
class RandXImpl(private val generator: RandGenerator = RealRandGenerator) : RandX {

    override fun randomBetween(from: Int, to: Int, except: Int?): Int {
        if (from < 0 || to < 0) throw IllegalArgumentException("No negative values are allowed: from=$from, to=$to")
        if (from >= to) throw IllegalArgumentException("From must be bigger then from: from=$from, to=$to")
        val diff = to - from
        if (except == null) {
            return generator.rand(diff) + from
        }
        if (except < from || except > to) throw IllegalArgumentException("Except value of '$except' must be within from=$from, to=$to")
        var randPos: Int?
        do {
            randPos = generator.rand(diff) + from
        } while (randPos == except)
        return randPos!!
    }

//    override fun <T> randomElementsExcept(items: List<T>, randomElementsCount: Int, except: T): List<T> {
//        if (items.size < randomElementsCount) {
//            throw IllegalArgumentException("items.size [${items.size}] < randomElementsCount [$randomElementsCount]")
//        }
//        val result = mutableListOf<T>()
//        var honeypot = items.toMutableList().minus(except)
//        while (result.size != randomElementsCount) {
//            val randomElement = randomOf(honeypot)
//            result += randomElement
//            honeypot = honeypot.minus(randomElement)
//        }
//        return result
//    }
//

//    override fun <T> randomOf(items: Array<T>, except: T): T {
//        if (items.size <= 1) throw IllegalArgumentException("array must contain at least 2 elements: $items")
//        var randItem: T?
//        do {
//            randItem = items[randomBetween(0, items.size - 1)]
//        } while (randItem == except)
//        return randItem!!
//    }
//
//    override fun <T> randomOf(items: List<T>) = items[randomBetween(0, items.size - 1)]
//    override fun <T> randomOf(items: List<T>, except: T): T {
//        // MINOR copy'n'paste from array
//        if (items.size <= 1) throw IllegalArgumentException("list must contain at least 2 elements: $items")
//        var randItem: T?
//        do {
//            randItem = items[randomBetween(0, items.size - 1)]
//        } while (randItem == except)
//        return randItem!!
//    }
//
//
//    private fun <T> Distribution<T>.sumOfPercents() = this.items.sumBy { it.percent }
}
