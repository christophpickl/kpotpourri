package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import java.util.HashMap


// ARRAY
// =====================================================================================================================

fun Array<out Any>.toPrettyString() =
        map { "- $it" }.joinToString("\n")

fun Array<out Any>.prettyPrint() {
    println(toPrettyString())
}


// LIST
// =====================================================================================================================

fun List<Any>.toPrettyString(prefix: String = "- ") =
        map { listItem -> "$prefix$listItem" }.joinToString("\n")

fun List<Any>.prettyPrint() {
    println(toPrettyString())
}


// MAP
// =====================================================================================================================

fun <K, V> Map<K, V>.verifyNoIntersection(that: Map<K, V>) {
    this.keys.forEach {
        if (that.containsKey(it)) {
            throw KPotpourriException("Expected no intersections! This: $this. That: $that.")
        }
    }
}


// ITERABLE
// =====================================================================================================================

fun <K, V> Iterable<Pair<K, V>>.toMutableMap(): Map<K, V> {
    val immutableMap = toMap()
    val map = HashMap<K, V>(immutableMap.size)
    map.putAll(immutableMap)
    return map
}
