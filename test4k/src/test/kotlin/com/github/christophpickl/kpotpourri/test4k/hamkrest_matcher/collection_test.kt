package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

@Test class HamkrestCollectionMatchersTest {

    private val MATCH = MatchResult.Match::class
    private val MISMATCH = MatchResult.Mismatch::class

    fun `containsExactlyInAnyOrder - Given a, When search a, Then matches`() {
        assertContainsExactlyInAnyOrder(listOf("a"), listOf("a"), true)
    }

    fun `containsExactlyInAnyOrder - Given a, When search b, Then fails`() {
        assertContainsExactlyInAnyOrder(listOf("a"), listOf("b"), false)
    }

    fun `containsExactlyInAnyOrder - Given a and b, When search for b and a, Then matches`() {
        assertContainsExactlyInAnyOrder(listOf("a", "b"), listOf("b", "a"), true)
    }

    fun `containsExactlyInAnyOrder - Given a and b, When search a, Then fails`() {
        assertContainsExactlyInAnyOrder(listOf("a", "b"), listOf("a"), false)
    }

    private fun assertContainsExactlyInAnyOrder(given: List<String>, contains: List<String>, shouldMatch: Boolean) {
        val expected = if (shouldMatch) MATCH else MISMATCH
        assertThat(containsExactlyInAnyOrder(*contains.toTypedArray())(given), isA(expected))
    }

    fun `containsExactlyInAnyOrder - Given invalid match, Then should contain array contents in description`() {
        assertThrown<AssertionError>({ e -> listOf("a", "b", "x", "y").all { e.message!!.contains(it) } }) {
            assertThat(listOf("a", "b"), containsExactlyInAnyOrder("x", "y"))
        }
    }

    fun `hasSizeOf - Given list of size 1, When assert hasSize of 1, Then matches`() {
        assertThat(hasSizeOf(1)(listOf("a")), isA(MATCH))
    }

    fun `hasSizeOf - Given list of size 2, When assert hasSize of 1, Then mismatches`() {
        assertThat(hasSizeOf(1)(listOf("a", "b")), isA(MISMATCH))
    }

}
