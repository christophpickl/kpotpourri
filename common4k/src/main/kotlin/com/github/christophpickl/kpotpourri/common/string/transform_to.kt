package com.github.christophpickl.kpotpourri.common.string

/**
 * Inspired by org.jetbrains.kotlin.cli.common.toBooleanLenient()
 */
fun String.toBooleanLenient2(): Boolean? = when (toLowerCase()) {
    in listOf("1", "yes", "true", "on", "y") -> true
    in listOf("0", "no", "false", "off", "n") -> false
    else -> null
}
