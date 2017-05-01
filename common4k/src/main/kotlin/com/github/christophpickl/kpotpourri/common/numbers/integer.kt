package com.github.christophpickl.kpotpourri.common.numbers

/**
 * Simply delegates to: `this in lower..upper`
 */
fun Int.isBetweenInclusive(lower: Int, upper: Int): Boolean {
    if (lower > upper) {
        throw IllegalArgumentException("lower $lower must be <= upper $upper")
    }
    @Suppress("ConvertTwoComparisonsToRangeCheck") // this in lower..upper; not as readable to me and f*cks up coverage report :-/
    return this >= lower && this <= upper
}

/**
 * Execute the given code `this` times often.
 */
fun Int.forEach(code: () -> Unit) {
    if (this <= 0) {
        throw IllegalArgumentException("Count must be at >= 1 but was: $this")
    }
    (1..this).forEach {
        code()
    }
}
