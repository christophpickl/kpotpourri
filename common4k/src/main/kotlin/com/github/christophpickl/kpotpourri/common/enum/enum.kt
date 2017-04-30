package com.github.christophpickl.kpotpourri.common.enum

import com.github.christophpickl.kpotpourri.common.collection.KPOT_DEFAULT_JOINER
import com.github.christophpickl.kpotpourri.common.collection.KPOT_DEFAULT_PREFIX
import com.github.christophpickl.kpotpourri.common.collection.toPrettyString
import kotlin.reflect.KClass

/**
 * Print all enum entries by their name each on a single line (can be configured).
 */
inline fun <reified T : Enum<T>> toPrettyString(prefix: String = KPOT_DEFAULT_PREFIX, joiner: String = KPOT_DEFAULT_JOINER) =
        enumValues<T>().toList().toPrettyString(prefix, joiner)

/**
 * Print all enum entries by their name in a comma separated format.
 *
 * Usage: printAllValues<MyEnum>()
 */
inline fun <reified T : Enum<T>> printAllValues() {
    println(enumValues<T>().joinToString { it.name })
}

/**
 * Print all enum entries by their name in a comma separated format.
 *
 * Usage: MyEnum::class.printAllValues()
 */
fun <T : Enum<*>> KClass<T>.printAllValues() {
    println(java
            .enumConstants
            .map { e -> e.name }
            .joinToString())
}
