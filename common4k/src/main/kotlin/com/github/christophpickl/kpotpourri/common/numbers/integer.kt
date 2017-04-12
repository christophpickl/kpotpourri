package com.github.christophpickl.kpotpourri.common.numbers

/**
 * Simply delegates to: `this in lower..upper`
 */
fun Int.isBetweenInclusive(lower: Int, upper: Int) = this in lower..upper

/**
 * Execute the given code `this` times often.
 */
fun Int.forEach(code: () -> Unit) {
    for (i in 1..this) {
        code()
    }
}
