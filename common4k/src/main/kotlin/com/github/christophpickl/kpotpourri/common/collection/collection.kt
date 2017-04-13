package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import java.util.HashMap

/** Default pretty print item prefix. */
val KPOT_DEFAULT_PREFIX = "- "
/** Default pretty print item joiner. */
val KPOT_DEFAULT_JOINER = "\n"

// ARRAY
// =====================================================================================================================

/**
 * Prints each item prefixed and joined.
 */
fun Array<out Any>.toPrettyString(prefix: String = KPOT_DEFAULT_PREFIX, joiner: String = KPOT_DEFAULT_JOINER) =
        map { prefix + it }.joinToString(joiner)

/**
 * Prints the output of `toPrettyString` to std out.
 */
fun Array<out Any>.prettyPrint(prefix: String = KPOT_DEFAULT_PREFIX, joiner: String = KPOT_DEFAULT_JOINER) {
    println(toPrettyString(prefix, joiner))
}


// LIST
// =====================================================================================================================

/**
 * Prints each item prefixed and joined.
 */
fun List<Any>.toPrettyString(prefix: String = KPOT_DEFAULT_PREFIX, joiner: String = KPOT_DEFAULT_JOINER) =
        map { prefix + it }.joinToString(joiner)

/**
 * Prints the output of `toPrettyString` to std out.
 */
fun List<Any>.prettyPrint(prefix: String = KPOT_DEFAULT_PREFIX, joiner: String = KPOT_DEFAULT_JOINER) {
    println(toPrettyString(prefix, joiner))
}


// MAP
// =====================================================================================================================

/**
 * Checks if there are the same keys for both maps.
 */
fun <K, V> Map<K, V>.hasIntersection(that: Map<K, V>): Boolean {
    return this.keys.firstOrNull { that.containsKey(it) } != null
}

/**
 * Throws a `KPotpourriException` if hasIntersection() is true
 */
fun <K, V> Map<K, V>.verifyNoIntersection(that: Map<K, V>) {
    if (hasIntersection(that)) {
        throw KPotpourriException("Expected no intersections! This: $this. That: $that.")
    }
}


// ITERABLE
// =====================================================================================================================

/**
 * listOf(1 to "einz").toMutableMap() == mapOf(1 to "einz")
 */
fun <K, V> Iterable<Pair<K, V>>.toMutableMap(): Map<K, V> {
    val immutableMap = toMap()
    val map = HashMap<K, V>(immutableMap.size)
    map.putAll(immutableMap)
    return map
}

// ENUM
// =====================================================================================================================

inline fun <reified T : Enum<T>> toPrettyString(prefix: String = KPOT_DEFAULT_PREFIX, joiner: String = KPOT_DEFAULT_JOINER) =
        enumValues<T>().toList().toPrettyString(prefix, joiner)