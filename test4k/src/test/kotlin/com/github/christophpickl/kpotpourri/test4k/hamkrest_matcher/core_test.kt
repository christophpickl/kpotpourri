package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.endsWith
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.startsWith
import org.testng.annotations.Test

@Test class CustomMatchersTest {

    //<editor-fold desc="allOf">

    fun `allOf - Given vararg which matches, Should not throw`() {
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
        assertThrown<IllegalArgumentException>({ e -> e.message!!.contains("empty") }) {
            assertThat("", allOf(emptyList()))
        }
    }

    fun `allOf - Given list which not matches, Should throw`() {
        assertThrown<AssertionError> {
            assertThat("fooxxx", allOf(listOf(startsWith("f"), endsWith("r"))))
        }
    }

    //</editor-fold>

    //<editor-fold desc="anyOf">

    fun `anyOf - Given vararg with one match, Should not throw`() {
        assertThat("foobar", anyOf(startsWith("f"), startsWith("r")))
    }

    fun `anyOf - Given vararg with no matches, Should throw`() {
        assertThrown<AssertionError> {
            assertThat("foobar", anyOf(startsWith("x"), startsWith("y")))
        }
    }

    fun `anyOf - Given list with one match, Should not throw`() {
        assertThat("foobar", anyOf(listOf(startsWith("f"), startsWith("r"))))
    }

    fun `anyOf - Given list with no match, Should throw`() {
        assertThrown<AssertionError> {
            assertThat("foobar", anyOf(listOf(startsWith("x"), startsWith("y"))))
        }
    }

    fun `anyOf - Given list which empty matchers, Should throw`() {
        assertThrown<IllegalArgumentException>({ e -> e.message!!.contains("empty") }) {
            assertThat("", anyOf(emptyList()))
        }
    }

    //</editor-fold>

    //<editor-fold desc="not">

    fun `not - Given non-equal objects should match`() {
        assertThat("x", not(equalTo("y")))
    }

    fun `not - Given equal objects should throw`() {
        assertThrown<AssertionError> {
            assertThat("x", not(equalTo("x")))
        }
    }

    //</editor-fold>

    //<editor-fold desc="notEqualTo">

    fun `notEqualTo - Given non-equal objects should match`() {
        assertThat("x", notEqualTo("y"))
    }

    fun `notEqualTo - Given equal objects should throw`() {
        assertThrown<AssertionError> {
            assertThat("x", notEqualTo("x"))
        }
    }

    //</editor-fold>

    //<editor-fold desc="nullValue">

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

    //</editor-fold>

    //<editor-fold desc="isA">

    fun `isA - Given isA String, When give a String, Then matches`() {
        assertThat("", isA(String::class))
    }

    fun `isA - Given isA Any, When give a String, Then matches`() {
        assertThat("", isA(Any::class))
    }

    fun `isA - Given isA String, When give an Int, Then not matches`() {
        assertThrown<AssertionError> {
            assertThat(1, isA(String::class))
        }
    }

    fun `isA - Given isA Any, When give an Int, Then matches`() {
        assertThrown<AssertionError> {
            assertThat(1, isA(Number::class))
        }
    }

    open class SuperType
    class SubType : SuperType()

    fun `isA - Given isA supertype, When give a subtype, Then not matches`() {
        assertThrown<AssertionError> {
            assertThat(SubType(), isA(SuperType::class))
        }
    }

    fun `isA - Given isA supertype, When give a supertype, Then matches`() {
        assertThrown<AssertionError> {
            assertThat(SuperType(), isA(SuperType::class))
        }
    }

    //</editor-fold>

}

