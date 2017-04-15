package com.github.christophpickl.kpotpourri.test4k

import org.testng.SkipException
import org.testng.annotations.Test

@Test class TestngTest {

    fun `skip throws`() {
        assertThrown<SkipException> {
            skip("any message")
        }
    }

}
