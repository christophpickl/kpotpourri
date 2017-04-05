package com.github.christophpickl.kpotpourri.common.exception

import com.github.christophpickl.kpotpourri.common.testinfra.assertThrown
import org.testng.annotations.Test

private class MyException() : RuntimeException()

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

}
