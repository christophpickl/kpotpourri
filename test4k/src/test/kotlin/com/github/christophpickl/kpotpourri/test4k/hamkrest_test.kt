package com.github.christophpickl.kpotpourri.test4k

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test


@Test class CustomShouldTest {

    fun `shouldMatchValue given equal objects should match`() {
        "x" shouldMatchValue  "x"
    }

    fun `shouldNotMatchValue given non-equal objects should match`() {
        "x" shouldNotMatchValue  "y"
    }

    @Test(expectedExceptions = arrayOf(AssertionError::class))
    fun `shouldMatchValue given non-equal objects should throw`() {
        "x" shouldMatchValue  "y"
    }

    @Test(expectedExceptions = arrayOf(AssertionError::class))
    fun `shouldNotMatchValue given equal objects should throw`() {
        "x" shouldNotMatchValue  "x"
    }

}

@Test class CustomMatchersTest {

    fun `not given non-equal objects should match`() {
        assertThat("x", not(equalTo("y")))
    }

    fun `notEqualTo given non-equal objects should match`() {
        assertThat("x", notEqualTo("y"))
    }

    @Test(expectedExceptions = arrayOf(AssertionError::class))
    fun `not given equal objects should throw`() {
//        assertThrown<AssertionError> {
        assertThat("x", not(equalTo("x")))
//        }
    }

    @Test(expectedExceptions = arrayOf(AssertionError::class))
    fun `notEqualTo given equal objects should throw`() {
//        assertThrown<AssertionError> {
        assertThat("x", notEqualTo("x"))
//        }
    }

}
