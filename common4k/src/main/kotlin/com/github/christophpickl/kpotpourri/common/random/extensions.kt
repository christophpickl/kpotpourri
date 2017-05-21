package com.github.christophpickl.kpotpourri.common.random

import java.util.Collections


/**
 * Returns a new copy of the very same list but with it's elements randomly shuffled.
 */
fun <T> List<T>.randomizeElements(): List<T> {
    return toMutableList().apply {
        Collections.shuffle(this)
    }
}


//fun <E> List<E>.randomElementsExcept(randomElementsCount: Int, except: E, rand: RandX = RandXImpl): List<E> {
//    return rand.randomElementsExcept(this, randomElementsCount, except)
//}
//
//
//fun <T> List<T>.randomElement(): T {
//    return RandXImpl.randomOf(this)
//}
//
//fun <T> Set<T>.randomElement(): T {
//    return RandXImpl.randomOf(this.toList())
//}

