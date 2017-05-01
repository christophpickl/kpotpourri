package com.github.christophpickl.kpotpourri.common.numbers

/**
 * Simply delegates to: `this in lower..upper`
 */
fun Int.isBetweenInclusive(lower: Int, upper: Int): Boolean {
    if (lower > upper) {
        throw IllegalArgumentException("lower $lower must be <= upper $upper")
    }
    return this in lower..upper
}

/**
 * Execute the given code `this` times often.
 */
fun Int.forEach(code: () -> Unit) {
    if (this <= 0) {
        throw IllegalArgumentException("Count must be at >= 1 but was: $this")
    }
    for (i in 1..this) {
        code()
    }
}
