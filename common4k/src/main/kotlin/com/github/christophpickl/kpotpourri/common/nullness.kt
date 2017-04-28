package com.github.christophpickl.kpotpourri.common

/**
 * Implicit null check and smart cast.
 *
 * This is: maybeNull.nullorWith(::DateTime)
 * Same as: maybeNull?.run { DateTime(this) }
 */
// MINOR test me
fun <IN, OUT> IN?.nullOrWith(wither: (IN) -> OUT): OUT? {
    if (this == null) {
        return null
    }
    return wither(this)
}
