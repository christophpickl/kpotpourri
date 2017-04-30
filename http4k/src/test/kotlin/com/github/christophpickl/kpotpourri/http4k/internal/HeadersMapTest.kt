package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.io.Io
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.mapContainsExactly
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.not
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

@Test class HeadersMapTest {

    companion object {
        private val KEY = "key"
        private val KEY_LOWER = "word"
        private val KEY_CAPITAL = "Word"
        private val KEY_UPPER = "WORD"
        private val VAL = "value"
        private val VAL1 = "value1"
        private val VAL2 = "value2"
        private val VAL3 = "value3"
    }

    fun `plusAssign - Given empty map, When add single entry, Then map contains exactly this entry`() {
        val map = HeadersMap()

        map += KEY to VAL
        assertThat(map.map, mapContainsExactly(KEY to VAL))
    }

    fun `plusAssign - When add two entries with same key, Then map contains only last entry`() {
        val map = HeadersMap()

        map += KEY to VAL1
        map += KEY to VAL2
        assertThat(map.map, mapContainsExactly(KEY to VAL2))
    }

    fun `addAll - When two entires, Then map contains only last entry`() {
        val map = HeadersMap()

        map.addAll(mapOf(KEY to VAL))
        assertThat(map.map, mapContainsExactly(KEY to VAL))
    }

    fun `plusAssign - When add two entries with key differ in casing, Then map contains only last entry`() {
        val map = HeadersMap()

        map += KEY_LOWER to VAL1
        assertThat(map.map, mapContainsExactly(KEY_LOWER to VAL1))
        map += KEY_CAPITAL to VAL2
        assertThat(map.map, mapContainsExactly(KEY_CAPITAL to VAL2))
        map += KEY_UPPER to VAL3
        assertThat(map.map, mapContainsExactly(KEY_UPPER to VAL3))
    }

    fun `Adding authorization secret gets not printed to stdout`() {
        val map = HeadersMap()
        val stdout = Io.readFromStdOut {
            map += "Authorization" to "secret"
        }
        // MINOR could also check if was not logged. but rather do that in an integration test ;)
        assertThat(stdout, not(containsSubstrings("secret")))
    }

}
