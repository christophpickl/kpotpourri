package com.github.christophpickl.kpotpourri.common

/**
 * Implicit null check and smart cast.
 *
 * This is: maybeNull.nullorWith(::DateTime)
 * Same as: maybeNull?.run { DateTime(this) }
 */
fun <IN, OUT> IN?.nullOrWith(wither: (IN) -> OUT): OUT? {
    if (this == null) {
        return null
    }
    return wither(this)
}

/**
 * Just like "maybeNull ?: throw Exception(message)" but overrides the message for both exceptions.
 */
fun <T> ensureSafe(message: String, function: () -> T?): T =
    try {
        function()
    } catch (e: Exception) {
        throw Exception(message, e)
    } ?: throw Exception(message)
