package com.github.christophpickl.kpotpourri.common.control


inline fun <reified X> takeFirstIfIs(first: Any, second: Any): X? {
    return when {
        first is X && second is X -> first
        first is X -> first
        second is X -> second
        else -> null
    }
}

fun throwIf(condition: Boolean, buildException: () -> Exception) {
    if (condition) {
        throw buildException()
    }
}
