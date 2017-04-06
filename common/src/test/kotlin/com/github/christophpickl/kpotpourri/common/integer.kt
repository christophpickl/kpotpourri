package com.github.christophpickl.kpotpourri.common


fun Int.isBetweenInclusive(lower: Int, upper: Int) = this >= lower && this <= upper

fun Int.forEach(code: () -> Unit) {
    for (i in 1..this) {
        code()
    }
}
