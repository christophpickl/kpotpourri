package com.github.christophpickl.kpotpourri.common.exception

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

private class MyException : RuntimeException()

@Test class ExceptionExtensionsTest {

    fun `tryOrRethrow - does nothing is nothing is thrown`() {
        MyException().tryOrRethrow { }
    }

    fun `tryOrRethrow - rethrows with cause initialised`() {
        val cause = RuntimeException()
        val exception = MyException()

        assertThrown<MyException>({ it.cause === cause }) {
            exception.tryOrRethrow { throw cause }
        }
    }

    fun `stackTraceAsString of Exception, Should print message and exception class name`() {
        val message = "test message"
        val exception = Exception(message)

        val actual = exception.stackTraceAsString()

        assertThat(actual, containsSubstrings(
                message, exception.javaClass.name))
    }

    fun `stackTraceAsString of Exception with cause, Should print messages and exception class names`() {
        val messageSuperficial = "test superficial message"
        val messageCause = "test root cause message"
        val cause = RuntimeException(messageCause)
        val exception = Exception(messageSuperficial, cause)

        val actual = exception.stackTraceAsString()

        assertThat(actual, containsSubstrings(
                messageSuperficial, exception.javaClass.name,
                messageCause, cause.javaClass.name))
    }

    fun `StackTrace formatted`() {
        val declaringClass = "declaringClass"
        val methodName = "methodName"
        val fileName = "fileName"
        val lineNumber = 42

        val actual = arrayOf(StackTraceElement(declaringClass, methodName, fileName, lineNumber)).formatted()

        assertThat(actual.size, equalTo(1))
        assertThat(actual[0], equalTo("$declaringClass#$methodName() at $fileName:$lineNumber"))
    }

}
