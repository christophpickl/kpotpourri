package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.describe

// TODO test this; should fail if there are additional items in this collection
fun <K> containsExactlyInAnyOrder(vararg expected: K): Matcher<Collection<K>> = object : Matcher.Primitive<Collection<K>>() {
    override fun invoke(actual: Collection<K>): MatchResult {
        return if (expected.all(actual::contains)) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch("was ${describe(actual)}")
        }
    }

    override val description: String get() = "contains ${describe(expected)}"
    override val negatedDescription: String get() = "does not contain ${describe(expected)}"
}

