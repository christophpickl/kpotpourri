package com.github.christophpickl.kpotpourri.common


/**
 * List pretty print.
 */
fun List<Any>.toPrettyString(prefix: String = "- ") = map { listItem -> "$prefix$listItem" }.joinToString("\n")

/**
 * Does some pretty printing by calling `toPrettyString`.
 */
fun List<Any>.prettyPrint() {
    println(toPrettyString())
}
