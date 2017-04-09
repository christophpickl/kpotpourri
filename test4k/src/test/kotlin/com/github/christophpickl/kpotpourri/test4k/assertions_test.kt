package com.github.christophpickl.kpotpourri.test4k

import org.testng.annotations.Test

@Test class AssertThrownTest {

    private val ANY_MESSAGE = "testMessage"

    fun `assertThrown without matcher - Given proper exception type, Should match type`() {
        assertThrown<MyException>() {
            throw MyException(ANY_MESSAGE)
        }
    }

    fun `assertThrown with matcher - Given exception matching message, Should match with same message`() {
        assertThrown<MyException>({ it.message == "foo" }) {
            throw MyException("foo")
        }
    }

    @Test(expectedExceptions = arrayOf(AssertionError::class))
    fun `assertThrown with matcher - Given exception not matching message, Should throw`() {
        assertThrown<MyException>({ it.message == "foo" }) {
            throw MyException("bar")
        }
    }

    @Test(expectedExceptions = arrayOf(AssertionError::class))
    fun `assertThrown - Given exception of different type, Should throw`() {
        assertThrown<MyException> {
            throw RuntimeException()
        }
    }

    fun `assertThrown - Given exception of sub type, Should match because of the simple IS check inside`() {
        assertThrown<MyException> {
            throw MySubException(ANY_MESSAGE)
        }
    }

}

private open class MyException(message: String) : RuntimeException(message)

private class MySubException(message: String) : MyException(message)
