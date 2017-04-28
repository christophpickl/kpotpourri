package com.github.christophpickl.kpotpourri.common.string

import com.github.christophpickl.kpotpourri.common.KPotpourriException

/**
 * Inspired by org.jetbrains.kotlin.cli.common.toBooleanLenient()
 */
// MINOR test me
fun String.toBooleanLenient2OrNull(): Boolean? = when (toLowerCase()) {
    in listOf("1", "yes", "true", "on", "y") -> true
    in listOf("0", "no", "false", "off", "n") -> false
    else -> null
}

fun String.toBooleanLenient2(): Boolean = when (toLowerCase()) {
    in listOf("1", "yes", "true", "on", "y") -> true
    in listOf("0", "no", "false", "off", "n") -> false
    else -> throw KPotpourriException("Unable to convert to boolean: '$this'!")
}
