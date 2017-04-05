package com.github.christophpickl.kpotpourri.common.testinfra

import com.natpryce.hamkrest.Matcher
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

infix fun <T> T.shouldMatchValue(expectedValue: T) {
    assertThat(this, equalTo(expectedValue))
}

infix fun <T> T.shouldNotMatchValue(expectedValue: T) {
    assertThat(this, not(equalTo(expectedValue)))
}

fun <T> notEqualTo(expected: T) =
        not(equalTo(expected))

//fun <T> not(delegate: Matcher<T?>): Matcher<T?> =
//        object : Matcher<T?> {
//            override fun invoke(actual: T?): MatchResult =
//            if (delegate.not().asPredicate().invoke(actual)) {
//                MatchResult.Match
//            } else {
//                MatchResult.Mismatch("was: ${describe(actual)}")
//            }
//
//            override val description: String get() = "should not() be: ${delegate.description}"
//            override val negatedDescription: String get() = "should not() not be: ${delegate.negatedDescription}"
//        }

fun <T> not(negated: Matcher<T?>): Matcher<T?> = Matcher.Negation(negated)
