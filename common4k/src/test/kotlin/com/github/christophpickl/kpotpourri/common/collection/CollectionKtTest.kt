package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.io.readFromStdOut
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.mapContainsExactly
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.isEmpty
import org.testng.annotations.DataProvider
import org.testng.annotations.Test


// ARRAY
// =====================================================================================================================

@Test class ArrayExtensionsTest {

    fun `toPrettyString given empty array should return empty string`() {
        assertThat(arrayOf<String>().toPrettyString(),
                equalTo(""))
    }

    fun `toPrettyString given single element should return single line`() {
        assertThat(arrayOf("a").toPrettyString(),
                equalTo("- a"))
    }

    fun `toPrettyString given two elements should return two lines`() {
        assertThat(arrayOf("a", "b").toPrettyString(),
                equalTo("- a\n- b"))
    }

    fun `toPrettyString custom prefix and joiner`() {
        assertThat(arrayOf("a", "b").toPrettyString("* ", "\t"),
                equalTo("* a\t* b"))
    }

    @DataProvider
    fun providePrettyPrintWithoutOptions(): Array<Array<out Any>> = arrayOf(
            arrayOf(emptyArray<String>(), "\n"),
            arrayOf(arrayOf("a"), "- a\n"),
            arrayOf(arrayOf("a", "b"), "- a\n- b\n")
    )

    @Test(dataProvider = "providePrettyPrintWithoutOptions")
    fun `prettyPrint - sunshine`(given: Array<String>, expected: String) {
        assertThat(readFromStdOut { given.prettyPrint() },
                equalTo(expected))
    }

}


// LIST
// =====================================================================================================================

@Test class ListExtensionsTest {

    fun `toPrettyString given empty list should return empty string`() {
        assertThat(emptyList<String>().toPrettyString(),
                equalTo(""))
    }

    fun `toPrettyString given single element should return single line`() {
        assertThat(listOf("a").toPrettyString(),
                equalTo("- a"))
    }

    fun `toPrettyString given two elements should return two lines`() {
        assertThat(listOf("a", "b").toPrettyString(),
                equalTo("- a\n- b"))
    }

    fun `toPrettyString custom prefix and joiner`() {
        assertThat(listOf("a", "b").toPrettyString("* ", "\t"),
                equalTo("* a\t* b"))
    }

    @DataProvider
    fun providePrettyPrintWithoutOptions(): Array<Array<out Any>> = arrayOf(
            arrayOf(emptyList<String>(), "\n"),
            arrayOf(listOf("a"), "- a\n"),
            arrayOf(listOf("a", "b"), "- a\n- b\n")
    )

    @Test(dataProvider = "providePrettyPrintWithoutOptions")
    fun `prettyPrint - sunshine`(given: List<String>, expected: String) {
        assertThat(readFromStdOut { given.prettyPrint() },
                equalTo(expected))
    }

    fun `plusIf ok`() {
        assertThat(emptyList<String>().plusIf(false, "a"), isEmpty)
        assertThat(emptyList<String>().plusIf(true, "a"), equalTo(listOf("a")))
    }

}


// MAP
// =====================================================================================================================

@Test class MapExtensionsTest {

    private val map1 = mapOf(1 to true)
    private val map2 = mapOf(2 to true)
    private val map12 = mapOf(1 to true, 2 to true)

    fun `hasIntersection`() {
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

}


// ITERABLE
// =====================================================================================================================

@Test class IterableExtensionsTest {

    fun `toMutableMap`() {
        assertThat(listOf(1 to "einz").toMutableMap(),
                equalTo(mapOf(1 to "einz")))
        assertThat(listOf(1 to "einz").toMutableMap(), isA<MutableMap<Int, String>>())
    }

}
