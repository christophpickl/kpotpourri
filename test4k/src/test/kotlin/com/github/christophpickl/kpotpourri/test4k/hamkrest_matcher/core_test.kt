package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.endsWith
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.startsWith
import org.testng.annotations.Test


@Test class CustomShouldTest {

    fun `shouldMatchValue given equal objects should match`() {
        "x" shouldMatchValue "x"
    }

    fun `shouldNotMatchValue given non-equal objects should match`() {
        "x" shouldNotMatchValue "y"
    }

    fun `shouldMatchValue given non-equal objects should throw`() {
        assertThrown<AssertionError> {
            "x" shouldMatchValue "y"
        }
    }

    fun `shouldNotMatchValue given equal objects should throw`() {
        assertThrown<AssertionError> {
            "x" shouldNotMatchValue "x"
        }
    }

}

@Test class CustomMatchersTest {

    fun `allOf - vararg sunshine`() {
        assertThat("foobar", allOf(startsWith("f"), endsWith("r")))
    }

    fun `allOf - vararg fail`() {
        assertThrown<AssertionError> {
            assertThat("fooxxx", allOf(startsWith("f"), endsWith("r")))
        }
    }

    fun `not given non-equal objects should match`() {
        assertThat("x", not(equalTo("y")))
    }

    fun `notEqualTo given non-equal objects should match`() {
        assertThat("x", notEqualTo("y"))
    }

    fun `not given equal objects should throw`() {
        assertThrown<AssertionError> {
            assertThat("x", not(equalTo("x")))
        }
    }

    fun `notEqualTo given equal objects should throw`() {
        assertThrown<AssertionError> {
            assertThat("x", notEqualTo("x"))
        }
    }

}
