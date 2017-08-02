@file:Suppress("KDocMissingDocumentation")

package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.describe

/**
 * Checks if the given map contains the key.
 */
fun <K, V> mapContainsKey(key: K): Matcher<Map<K, V>> = object : Matcher.Primitive<Map<K, V>>() {
    override fun invoke(actual: Map<K, V>): MatchResult {
        return if (actual.containsKey(key)) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch("was ${describe(actual)}")
        }
    }

    override val description: String get() = "contains ${describe(key)}"
    override val negatedDescription: String get() = "does not contain ${describe(key)}"
}

/**
 * Checks if the actual Map contains at least the given entry.
 */
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

/**
 * Checks if the actual Map contains _exactly_ the given entries, ignoring the order.
 */
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

/**
 * Should actually be in common4k but that would introduce a cyclic dependency.
 */
private fun <K, V> Map<K, V>.containsExact(other: Map<K, V>): Boolean {
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
