package com.github.christophpickl.kpotpourri.test4k

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.describe
import com.natpryce.hamkrest.equalTo


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

fun <K, V> mapContains(entry: Pair<K, V>): Matcher<Map<K, V>> = object : Matcher.Primitive<Map<K, V>>() {
    override fun invoke(actual: Map<K, V>): MatchResult {
        return if (actual.containsKey(entry.first) && actual[entry.first] == entry.second) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch("was ${describe(actual)}")
        }
    }
    override val description: String get() = "contains ${describe(entry)}"
    override val negatedDescription: String get() = "does not contain ${describe(entry)}"
}
