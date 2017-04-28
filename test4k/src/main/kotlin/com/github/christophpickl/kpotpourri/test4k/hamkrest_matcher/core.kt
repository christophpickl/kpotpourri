package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo

// Matcher<String> hasLength

fun <T> allOf(vararg matchers: Matcher<T>): Matcher<T> =
        allOf(matchers.toList())

fun <T> allOf(matchers: List<Matcher<T>>): Matcher<T> {
    var allMatcher = matchers[0]
    matchers.forEachIndexed { i, matcher ->
        if (i != 0) {
            allMatcher = allMatcher and matcher
        }
    }
    return allMatcher
}

fun <T> not(negated: Matcher<T?>): Matcher<T?> = Matcher.Negation(negated)

fun notNullValue() = not(equalTo(null))
fun nullValue() = equalTo(null)

fun <T> notEqualTo(expected: T) =
        not(equalTo(expected))

fun Boolean.shouldBeTrue() {
    this shouldMatchValue true
}

fun Boolean.shouldBeFalse() {
    this shouldMatchValue false
}

infix fun <T> T.shouldMatchValue(expectedValue: T) {
    assertThat(this, equalTo(expectedValue))
}

infix fun <T> T.shouldNotMatchValue(expectedValue: T) {
    assertThat(this, not(equalTo(expectedValue)))
}


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
