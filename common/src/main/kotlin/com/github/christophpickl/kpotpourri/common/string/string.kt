package com.github.christophpickl.kpotpourri.common.string

/**
 * Returns `null` if `isEmpty()` evaluates to true.
 */
fun String.nullIfEmpty() = if (isEmpty()) null else this
