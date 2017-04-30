package com.github.christophpickl.kpotpourri.common.string

import com.github.christophpickl.kpotpourri.common.KPotpourriException


/**
 * Inspired by org.jetbrains.kotlin.cli.common.toBooleanLenient()
 */
fun String.toBooleanLenient2(): Boolean = toBooleanLenient2OrNull() ?:
        throw KPotpourriException("Unable to convert to boolean: '$this'!")

/**
 * Inspired by org.jetbrains.kotlin.cli.common.toBooleanLenient()
 */
fun String.toBooleanLenient2OrNull(): Boolean? = when (toLowerCase()) {
    in listOf("1", "yes", "true", "on", "y") -> true
    in listOf("0", "no", "false", "off", "n") -> false
    else -> null
}
