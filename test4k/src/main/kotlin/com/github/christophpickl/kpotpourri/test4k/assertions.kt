package com.github.christophpickl.kpotpourri.test4k

import org.testng.Assert
import java.io.PrintWriter
import java.io.StringWriter


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
 * Tests for correct exception type (or supertype).
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
 * Tests for correct exception type and message subparts.
 */
inline fun <reified E : Throwable> assertThrown(expectedMessageParts: List<String>, code: () -> Unit) {
    assertThrown<E>({ it.message!!.__containsAll(expectedMessageParts) }, code)
}

/**
 * Duplicate from common4k, in order to avoid cyclic dependency.
 */
fun String.__containsAll(substrings: List<String>, ignoreCase: Boolean = false) =
        substrings.all { this.contains(it, ignoreCase) }

/**
 * Expect an exception to be thrown which can be individually checked for correctness.
 */
inline fun <reified E : Throwable> assertThrown(matcher: (E) -> Boolean, code: () -> Unit) {
    val expectedExceptionType = E::class.java.simpleName
    try {
        code()
        fail("Expected an exception to be thrown of type $expectedExceptionType")
    } catch (t: Throwable) {
        if (t !is E) {
            fail("Unexpected exception type (${t.javaClass.simpleName}) was thrown!\n${t.toStackTrace()}")
        } else if (!matcher(t)) {
            fail("Unexpected exception was thrown! Expected a $expectedExceptionType but was thrown:\n${t.toStackTrace()}")
        }
        // thrown exception matched, everything is OK
    }
}

fun Throwable.toStackTrace(): String {
    val writer = StringWriter()
    printStackTrace(PrintWriter(writer))
    return writer.toString()
}
