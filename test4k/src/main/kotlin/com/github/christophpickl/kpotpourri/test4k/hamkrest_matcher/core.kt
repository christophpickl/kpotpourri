package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.or
import kotlin.reflect.KClass

/**
 * Hamcrest matcher to check if all given matchers match.
 */
fun <T> allOf(vararg matchers: Matcher<T>): Matcher<T> =
        allOf(matchers.toList())

/**
 * Hamcrest matcher to check if all given matchers match.
 */
fun <T> allOf(matchers: Collection<Matcher<T>>): Matcher<T> {
    if (matchers.isEmpty()) {
        throw IllegalArgumentException("Passed matchers must not be empty!")
    }
    var allMatcher = matchers.first()
    matchers.forEachIndexed { i, matcher ->
        if (i != 0) {
            allMatcher = allMatcher and matcher
        }
    }
    return allMatcher
}

/**
 * Hamcrest matcher to check if any of the given matchers match.
 */
fun <T> anyOf(vararg matchers: Matcher<T>): Matcher<T> =
        anyOf(matchers.toList())

/**
 * Hamcrest matcher to check if any of the given matchers match.
 */
fun <T> anyOf(matchers: Collection<Matcher<T>>): Matcher<T> {
    if (matchers.isEmpty()) {
        throw IllegalArgumentException("Passed matchers must not be empty!")
    }
    var allMatcher = matchers.first()
    matchers.forEachIndexed { i, matcher ->
        if (i != 0) {
            allMatcher = allMatcher or matcher
        }
    }
    return allMatcher
}

/**
 * Hamcrest matcher to negate a matcher for nullables.
 */
fun <T> notMaybeNull(negated: Matcher<T?>): Matcher<T?> = Matcher.Negation(negated)

/**
 * Hamcrest matcher to negate a matcher.
 */
fun <T> not(negated: Matcher<T>): Matcher<T> = Matcher.Negation(negated)

/**
 * Hamcrest shortcut matcher to check for non-equality.
 */
fun <T> notEqualTo(expected: T) =
        not(equalTo(expected))

/**
 * Hamcrest shortcut matcher to check for non-nullness.
 */
@Suppress("UNCHECKED_CAST")
fun <T> notNullValue() = not(equalTo(null as T))

/**
 * Hamcrest shortcut matcher to check for nullness.
 */
@Suppress("UNCHECKED_CAST")
fun <T> nullValue() = equalTo(null as T)

/**
 * Same as Hamkrest's, but not using reified parameters (necessary for increased readbililty, AAA).
 */
@Suppress("KDocMissingDocumentation")
fun <T : Any> isA(expected: KClass<T>) =
        object : Matcher<Any> {
            override fun invoke(actual: Any) =
                    if (expected.isInstance(actual)) {
                        MatchResult.Match
                    } else {
                        MatchResult.Mismatch("was: a ${actual.javaClass.kotlin.qualifiedName}")
                    }

            override val description: String
                get() = "is a ${expected.qualifiedName}"
        }
