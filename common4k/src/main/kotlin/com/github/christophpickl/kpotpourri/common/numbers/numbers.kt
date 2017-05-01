package com.github.christophpickl.kpotpourri.common.numbers


/**
 * Format a double into a nice displayable representation.
 *
 * Example: (0.1234).format(2) => 0.12
 */
fun Double.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)

/**
 * This == milliseconds
 *
 * Example: (1234.5678).toSeconds() => 1.235 secs
 */
fun Double.toSeconds(
        digits: Int = 3,
        suffix: String = " secs"
): String = (this / 1000.0).format(digits) + suffix
