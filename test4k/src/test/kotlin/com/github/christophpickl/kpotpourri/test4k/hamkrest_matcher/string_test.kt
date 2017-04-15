package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

@Test class StringTest {

    fun `contains substrings`() {
        assertThat("a b c d", containsSubstrings("a", "b", "c", "d"))
    }

    fun `contains substrings - fails`() {
        assertThrown<AssertionError> {
            assertThat("a", containsSubstrings("x"))
        }
    }

}
