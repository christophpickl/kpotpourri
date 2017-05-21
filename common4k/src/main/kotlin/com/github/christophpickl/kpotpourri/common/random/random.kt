package com.github.christophpickl.kpotpourri.common.random

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import java.lang.IllegalArgumentException


/**
 * Random generator as an interface in order to be testable.
 */
interface RandX {

    /**
     * Generate a random number based on the given range (inclusive), optionally specifying an item to be excluded.
     */
    fun randomBetween(from: Int, to: Int, except: Int? = null): Int

    /**
     * Retrieve a single element from the given list, optionally specifying an item to be excluded.
     */
    fun <T> randomOf(items: List<T>, exceptItem: T? = null): T

//    fun <T> randomElementsExcept(items: List<T>, randomElementsCount: Int, except: T): List<T>
}

private val MAX_ITERATIONS = 9000
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
        var count = 0
        do {
            randPos = generator.rand(diff) + from
            if (count++ >= MAX_ITERATIONS) {
                throw KPotpourriException("Maximum random iterations was reached! Generator: $generator")
            }
        } while (randPos == except)
        return randPos!!
    }

    //    override fun <T> randomOf(items: List<T>) = items[randomBetween(0, items.size - 1)]
    override fun <T> randomOf(items: List<T>, exceptItem: T?): T {
        if (items.isEmpty()) throw IllegalArgumentException("list must not be empty")
        if (items.size == 1) return items[0]

        var randItem: T?
        var count = 0
        do {
            randItem = items[randomBetween(0, items.size - 1)]
            if (count++ >= MAX_ITERATIONS) {
                throw KPotpourriException("Maximum random iterations was reached! Generator: $generator")
            }
        } while (randItem == exceptItem)
        return randItem!!
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

//
//
//
}
