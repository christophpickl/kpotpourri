package com.github.christophpickl.kpotpourri.common


/**
 * List pretty print.
 */
fun List<Any>.toPrettyString() = map { "- ${it.toString()}" }.joinToString("\n")

/**
 * Does some pretty printing by calling [toPrettyString].
 *
 * @see toPrettyString
 */
fun List<Any>.prettyPrint() {
    println(toPrettyString())
}
