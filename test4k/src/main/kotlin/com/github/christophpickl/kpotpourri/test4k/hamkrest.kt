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

fun <K, V> mapContainsExactly(vararg entries: Pair<K, V>): Matcher<Map<K, V>> = object : Matcher.Primitive<Map<K, V>>() {

    private val entriesDescribed: String by lazy { describe(entries.joinToString(", ")) }
    private val expectedEntries: Map<K, V> by lazy { entries.toMap() }

    override fun invoke(actual: Map<K, V>): MatchResult {
        return if (actual.size == entries.size && actual.containsExact(expectedEntries)) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch("was ${describe(actual)}")
        }
    }

    override val description: String get() = "contains exactly $entriesDescribed"
    override val negatedDescription: String get() = "does not contain exactly $entriesDescribed"
}

// MINOR TEST this; should actually be in commons, but as of cyclic dependency otherwise... :-/
fun <K, V> Map<K, V>.containsExact(other: Map<K, V>): Boolean {
    this.entries.forEach { (k, v) ->
        if (!other.contains(k) || other[k] != v) {
            return false
        }
    }
    other.entries.forEach { (k, v) ->
        if (!this.contains(k) || this[k] != v) {
            return false
        }
    }
    return true
}
