package com.github.christophpickl.kpotpourri.common.numbers


/**
 * Format a double into a nice displayable representation.
 */
fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)
