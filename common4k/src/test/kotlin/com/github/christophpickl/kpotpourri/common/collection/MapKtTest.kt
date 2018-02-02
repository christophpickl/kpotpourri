package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.io.Io
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.mapContainsExactly
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.not
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test


@Test class MapKtTest {

    private val map1 = mapOf(1 to true)
    private val map2 = mapOf(2 to true)
    private val map12 = mapOf(1 to true, 2 to true)

    fun `hasIntersection sunshine`() {
        assertThat(map1.hasIntersection(map2), equalTo(false))
        assertThat(map1.hasIntersection(map12), equalTo(true))
    }

    fun `verifyNoIntersection does not throw exception when has no intersection`() {
        map1.verifyNoIntersection(map2)
    }

    @Test(expectedExceptions = arrayOf(KPotpourriException::class))
    fun `verifyNoIntersection throws exception when has intersection`() {
        map1.verifyNoIntersection(map12)
    }

    fun `put a Pair`() {
        assertThat(mutableMapOf<String, String>().apply { put("a" to "b") },
                mapContainsExactly("a" to "b"))
    }

    @DataProvider
    fun provideMapsOfMaps(): Array<Array<out Any>> = arrayOf(
            arrayOf(arrayOf(emptyMap<Any, Any>()), emptyMap<Any, Any>()),
            arrayOf(arrayOf(mapOf(1 to 2)), mapOf(1 to 2)),
            arrayOf(arrayOf(mapOf(1 to 2), mapOf("a" to "b")), mapOf(1 to 2, "a" to "b")),
            arrayOf(arrayOf(mapOf("x" to 1), mapOf("x" to 2)), mapOf("x" to 2))
    )

    @Test(dataProvider = "provideMapsOfMaps")
    fun `mapsOf - sunshine`(givenMaps: Array<Map<Any, Any>>, expected: Map<Any, Any>) {
        // MINOR (ex-FIX-ME) kotlin compiler cannot resolve reference!!! ??? !!! ???
//        mapsOf(*givenMaps) shouldMatchValue expected
    }

}


@Test class KeyIgnoringCaseMapTest {

    companion object {
        private val KEY = "key"
        private val KEY_LOWER = "word"
        private val KEY_CAPITAL = "Word"
        private val KEY_UPPER = "WORD"
        private val VAL = "value"
        private val VAL1 = "value1"
        private val VAL2 = "value2"
        private val VAL3 = "value3"
        private val SECRET_KEY = "secretAuth"
    }

    private lateinit var map: KeyIgnoringCaseMap<String>

    @BeforeMethod fun resetMap() {
        map = KeyIgnoringCaseMap(disableLoggingOfKeys = listOf(SECRET_KEY))
    }

    fun `plusAssign - Given empty map, When add single entry, Then map contains exactly this entry`() {
        map += KEY to VAL

        assertThat(map._map, mapContainsExactly(KEY to VAL))
    }

    fun `plusAssign - When add two entries with same key, Then map contains only last entry`() {
        map += KEY to VAL1
        map += KEY to VAL2

        assertThat(map._map, mapContainsExactly(KEY to VAL2))
    }

    fun `addAll - When two entires, Then map contains only last entry`() {
        map.addAll(mapOf(KEY to VAL))

        assertThat(map._map, mapContainsExactly(KEY to VAL))
    }

    fun `plusAssign - When add two entries with key differ in casing, Then map contains only last entry`() {
        map += KEY_LOWER to VAL1
        assertThat(map._map, mapContainsExactly(KEY_LOWER to VAL1))

        map += KEY_CAPITAL to VAL2
        assertThat(map._map, mapContainsExactly(KEY_CAPITAL to VAL2))

        map += KEY_UPPER to VAL3
        assertThat(map._map, mapContainsExactly(KEY_UPPER to VAL3))
    }

    fun `Adding authorization secret gets not printed to stdout`() {
        val stdout = Io.readFromStdOut {
            map += SECRET_KEY to "secret value"
        }
        // MINOR could also check if was not logged
        assertThat(stdout, not(containsSubstrings("secret")))
    }

}

