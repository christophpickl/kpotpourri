package com.github.christophpickl.kpotpourri.common.collection

import java.util.*


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

/**
 * Handy method to add an element under a certain condition and return the new list.
 */
fun <T> List<T>.plusIf(condition: Boolean, elementToAdd: T): List<T> {
    return if (condition) {
        plus(elementToAdd)
    } else {
        this
    }
}

fun combineMaybeEmpty(first: String, other: List<String>): List<String> {
    val list = ArrayList<String>(1 + other.size)
    if (first.isNotEmpty()) {
        list += first
    }
    list += other
    return list
}
