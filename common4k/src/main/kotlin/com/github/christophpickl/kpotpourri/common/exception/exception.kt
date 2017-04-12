package com.github.christophpickl.kpotpourri.common.exception

/**
 * Invokes the given lambda, catches any Exception and init it as the cause of `this`.
 *
 * Usage:
 *
 * RuntimeException("my message").tryOrRethrow {
 *   somethingWhichThrows()
 * }
 */
fun <E: RuntimeException> E.tryOrRethrow(action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        throw this.initCause(e)
    }
}
