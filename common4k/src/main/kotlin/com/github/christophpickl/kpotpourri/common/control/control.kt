package com.github.christophpickl.kpotpourri.common.control

/**
 * Checks if given items are of type X, whereas first has precedence over second if both are X.
 */
inline fun <reified X> takeFirstIfIs(first: Any, second: Any): X? {
    return when {
        first is X -> first
        second is X -> second
        else -> null
    }
}

/**
 * Conditional throw in order to be able to throw exceptions in when statements as expressions.
 */
fun throwIf(condition: Boolean, buildException: () -> Exception) {
    if (condition) {
        throw buildException()
    }
}
