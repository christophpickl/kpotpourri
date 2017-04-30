package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.KPotpourriException


/**
 * Checks if there are the same keys for both maps.
 */
fun <K, V> Map<K, V>.hasIntersection(that: Map<K, V>): Boolean {
    return this.keys.firstOrNull(that::containsKey) != null
}

/**
 * Throws a `KPotpourriException` if hasIntersection() is true
 */
fun <K, V> Map<K, V>.verifyNoIntersection(that: Map<K, V>) {
    if (hasIntersection(that)) {
        throw KPotpourriException("Expected no intersections! This: $this. That: $that.")
    }
}

/**
 * Same as original put() but with Kotlin's Pair instance to allow the "to" syntax.
 *
 * Sample: put(1, "1") => put(1 to "1")
 */
fun <K, V> MutableMap<K, V>.put(pair: Pair<K, V>) =
        put(pair.first, pair.second)
