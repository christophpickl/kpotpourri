package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.describe

/**
 * Checks if the given collection contains exactly (not more or less) elements in the very same order.
 */
@Suppress("KDocMissingDocumentation")
fun <K> containsExactlyInOrder(vararg expected: K) = object : Matcher.Primitive<Collection<K>>() {

    override fun invoke(actual: Collection<K>): MatchResult {
        if (expected.size != actual.size) return MatchResult.Mismatch("was ${describe(actual)} (length mismatch)")
        actual.forEachIndexed { i, v ->
            if (expected[i] != v) {
                return MatchResult.Mismatch("was ${describe(actual)}")
            }
        }
        return MatchResult.Match
    }

    override val description: String get() = "contains ${expected.contentToString()}"
    override val negatedDescription: String get() = "does not contain ${expected.contentToString()}"
}


/**
 * Checks if the given collection contains exactly (not more or less) elements in an undefined order.
 */
@Suppress("KDocMissingDocumentation")
fun <K> containsExactlyInAnyOrder(vararg expected: K) = object : Matcher.Primitive<Collection<K>>() {

    override fun invoke(actual: Collection<K>) =
            if (expected.all(actual::contains) && actual.all { it in expected }) {
                MatchResult.Match
            } else {
                MatchResult.Mismatch("was ${describe(actual)}")
            }

    override val description: String get() = "contains ${expected.contentToString()}"
    override val negatedDescription: String get() = "does not contain ${expected.contentToString()}"
}

/**
 * Simplify standard usage of `hasSize(equalTo(1))` into: `hasSizeOf(1)` (passing literal rather a matcher)
 */
@Suppress("KDocMissingDocumentation")
fun hasSizeOf(expectedSize: Int) = object : Matcher.Primitive<Collection<Any>>() {

    override fun invoke(actual: Collection<Any>) = if (actual.size == expectedSize) {
        MatchResult.Match
    } else {
        MatchResult.Mismatch("was ${describe(actual)}")
    }

    override val description: String get() = "size of $expectedSize"
    override val negatedDescription: String get() = "not size of $expectedSize"
}
