package com.github.christophpickl.kpotpourri.test4k

import org.testng.Assert


/**
 * Global function supporting nulls as causes.
 */
fun fail(message: String, cause: Throwable? = null) {
    if (cause != null) {
        Assert.fail(message, cause)
    } else {
        Assert.fail(message) // cause must not be null
    }
}


/**
 * Tests for correct exception type only.
 */
inline fun <reified E : Throwable> assertThrown(code: () -> Unit) {
    assertThrown<E>({ true }, code)
}


/**
 * Tests for correct exception type and message.
 */
inline fun <reified E : Throwable> assertThrown(expectedMessage: String, code: () -> Unit) {
    assertThrown<E>({ it.message == expectedMessage }, code)
}


/**
 * Expect an exception to be thrown which can be individually checked for correctness.
 */
inline fun <reified E : Throwable> assertThrown(matcher: (E) -> Boolean, code: () -> Unit) {
    try {
        code()
        fail("Expected an exception to be thrown of type ${E::class.java.simpleName}")
    } catch (e: Exception) {
        if (e !is E) {
            fail("Unexpected exception type (${e.javaClass.simpleName}) was thrown: $e")
        } else if (!matcher(e)) {
            fail("Unexpected exception was thrown: $e")
        }
        // thrown exception matched, everything is OK
    }
}
