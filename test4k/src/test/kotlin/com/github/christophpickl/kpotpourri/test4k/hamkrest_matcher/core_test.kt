package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.endsWith
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.startsWith
import org.testng.annotations.Test

@Test class CustomMatchersTest {

    fun `allOf - Given vararg which mathes, Should not throw`() {
        assertThat("foobar", allOf(startsWith("f"), endsWith("r")))
    }

    fun `allOf - Given vararg which not matches, Should throw`() {
        assertThrown<AssertionError> {
            assertThat("fooxxx", allOf(startsWith("f"), endsWith("r")))
        }
    }

    fun `allOf - Given list which matches, Should not throw`() {
        assertThat("foobar", allOf(listOf(startsWith("f"), endsWith("r"))))
    }

    fun `allOf - Given list which empty matchers, Should throw`() {
        assertThrown<IllegalArgumentException>({ e -> e.message!!.contains("empty")}) {
            assertThat("", allOf(emptyList()))
        }
    }

    fun `allOf - Given list which not matches, Should throw`() {
        assertThrown<AssertionError> {
            assertThat("fooxxx", allOf(listOf(startsWith("f"), endsWith("r"))))
        }
    }

    fun `not - Given non-equal objects should match`() {
        assertThat("x", not(equalTo("y")))
    }

    fun `not - Given equal objects should throw`() {
        assertThrown<AssertionError> {
            assertThat("x", not(equalTo("x")))
        }
    }

    fun `notEqualTo - Given non-equal objects should match`() {
        assertThat("x", notEqualTo("y"))
    }

    fun `notEqualTo - Given equal objects should throw`() {
        assertThrown<AssertionError> {
            assertThat("x", notEqualTo("x"))
        }
    }

    fun `nullValue - Given null string, Should not throw`() {
        assertThat(null as String?, nullValue())
    }

    fun `nullValue - Given empty string, Should throw`() {
        assertThrown<AssertionError> {
            assertThat("", nullValue())
        }
    }

    fun `notNullValue - Given empty string, Should not throw`() {
        assertThat("", notNullValue())
    }

    fun `notNullValue - Given null string, Should throw`() {
        assertThrown<AssertionError> {
            assertThat(null as String?, notNullValue())
        }
    }

}
