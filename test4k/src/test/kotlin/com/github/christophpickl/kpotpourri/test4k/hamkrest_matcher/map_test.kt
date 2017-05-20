package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

@Test class MapTest {

    fun `mapContainsKey - ok`() {
        mapContainsKey<String, String>("a").invoke(mapOf("a" to "b")) shouldMatchValue MatchResult.Match
    }

    fun `mapContainsKey - nok`() {
        assertThat(mapContainsKey<String, String>("a").invoke(emptyMap()), isA(MatchResult.Mismatch::class))
    }

    fun `mapContains - Given exact map, Should not throw`() {
        assertThat(mapOf("a" to "b"), mapContains("a" to "b"))
    }

    fun `mapContains - Given more entries but with expected entry, Should not throw`() {
        assertThat(mapOf("a" to "b", "c" to "d"), mapContains("a" to "b"))
    }

    fun `mapContains - Given empty map, Should throw`() {
        assertThrown<AssertionError> {
            assertThat(emptyMap(), mapContains("a" to "b"))
        }
    }

    fun `mapContains - Given invalid entries, Should throw`() {
        assertThrown<AssertionError> {
            assertThat(mapOf("x" to "y"), mapContains("a" to "b"))
        }
    }

    fun `mapContainsExactly - Given exact map, Should not throw`() {
        assertThat(mapOf("a" to "b"), mapContainsExactly("a" to "b"))
    }

    fun `mapContainsExactly - Given correct but also additional entry, Should throw`() {
        assertThrown<AssertionError> {
            assertThat(mapOf("a" to "b", "c" to "d"), mapContainsExactly("a" to "b"))
        }
    }

    fun `mapContainsExactly - Given empty map, Should not throw`() {
        assertThat(emptyMap<String, String>(), mapContainsExactly())
    }

    fun `mapContainsExactly - Given exact map with two elements, Should not throw`() {
        assertThat(mapOf("a" to "b", "c" to "d"), mapContainsExactly("a" to "b", "c" to "d"))
    }

}
