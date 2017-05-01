package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.describe

/**
 * Checks if the given collection contains exactly (not more or less) elements in an undefined order.
 */
@Suppress("KDocMissingDocumentation")
fun <K> containsExactlyInAnyOrder(vararg expected: K): Matcher<Collection<K>> = object : Matcher.Primitive<Collection<K>>() {
    override fun invoke(actual: Collection<K>): MatchResult {
        return if (expected.all(actual::contains) && actual.all { it in expected }) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch("was ${describe(actual)}")
        }
    }

    override val description: String get() = "contains ${expected.contentToString()}"
    override val negatedDescription: String get() = "does not contain ${expected.contentToString()}"
}

