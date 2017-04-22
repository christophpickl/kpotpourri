package com.github.christophpickl.kpotpourri.common.numbers


/**
 * Format a double into a nice displayable representation.
 */
fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)


/**
 * This == milliseconds
 */
fun Double.toSeconds(
        digits: Int = 3,
        suffix: String = " secs"
): String = (this / 1000.0).format(digits) + suffix
