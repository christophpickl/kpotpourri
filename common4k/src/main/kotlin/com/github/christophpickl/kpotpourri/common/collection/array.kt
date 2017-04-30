package com.github.christophpickl.kpotpourri.common.collection


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
