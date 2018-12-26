package com.github.christophpickl.kpotpourri.common.string

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File


@Test class StringExtensionsTest {

    @DataProvider
    fun nullIfEmptyProvider(): Array<Array<out Any?>> = arrayOf(
            arrayOf("a", "a"),
            arrayOf(" ", " "),
            arrayOf("", null)
    )

    @Test(dataProvider = "nullIfEmptyProvider")
    fun `nullIfEmpty`(given: String, expected: String?) {
        assertThat(given.nullIfEmpty(), equalTo(expected))
    }

    @DataProvider fun timesProvider(): Array<Array<Any>> = arrayOf(
            arrayOf("x", 0, ""),
            arrayOf("x", 1, "x"),
            arrayOf("x", 2, "xx")
    )

    @Test(dataProvider = "timesProvider")
    fun `times`(symbol: String, count: Int, expected: String) {
        assertThat(symbol.times(count),
                equalTo(expected))
    }

    fun `wrapIf`() {
        assertThat("a".wrapIf(false, "1", "2"), equalTo("a"))
        assertThat("a".wrapIf(true, "1", "2"), equalTo("1a2"))
    }

    fun `wrapParenthesisIf`() {
        assertThat("a".wrapParenthesisIf(false), equalTo("a"))
        assertThat("a".wrapParenthesisIf(true), equalTo("(a)"))
    }

    fun `removePreAndSuffix`() {
        assertThat("-a-".removePreAndSuffix("-"), equalTo("a"))
        assertThat("a-a".removePreAndSuffix("-"), equalTo("a-a"))
    }

    fun `htmlize`() {
        assertThat("a".htmlize(), equalTo("<html>a</html>"))
        assertThat("a\nb".htmlize(), equalTo("<html>a<br/>b</html>"))
    }

    @DataProvider
    fun provideConcatUrls(): Array<Array<out Any>> = arrayOf(
            arrayOf("", "", ""),
            arrayOf("http://host.at", "", "http://host.at"),
            arrayOf("", "http://host.at", "http://host.at"),
            arrayOf("http://host.at/", "", "http://host.at"),
            arrayOf("http://host.at/", "/", "http://host.at"),
            arrayOf("http://host.at", "endpoint", "http://host.at/endpoint"),
            arrayOf("http://host.at/", "endpoint", "http://host.at/endpoint"),
            arrayOf("http://host.at", "/endpoint", "http://host.at/endpoint"),
            arrayOf("http://host.at/", "/endpoint", "http://host.at/endpoint"),
            arrayOf("http://host.at/", "/endpoint/", "http://host.at/endpoint")
    )

    @Test(dataProvider = "provideConcatUrls")
    fun `concat urls`(part1: String, part2: String, expected: String) {
        assertThat(concatUrlParts(part1, part2), equalTo(expected))
        // aliase
        assertThat(joinUrlParts(part1, part2), equalTo(expected))
        assertThat(combineUrlParts(part1, part2), equalTo(expected))
    }

    fun `concat some misc urls`() {
        assertThat(concatUrlParts("a", "b", "c", "d"), equalTo("a/b/c/d"))
        assertThat(concatUrlParts("/with", "leading"), equalTo("/with/leading"))
        assertThat(concatUrlParts("with", "trailing/"), equalTo("with/trailing"))
        assertThat(concatUrlParts("/with", "leading", "and", "trailing/"), equalTo("/with/leading/and/trailing"))
    }

    @DataProvider
    fun provideSplitAsArguments(): Array<Array<out Any>> = arrayOf(
            arrayOf("", emptyList<String>()),
            arrayOf(" ", emptyList<String>()),
            arrayOf("\t\n ", emptyList<String>()),
            arrayOf("a", listOf("a")),
            arrayOf(" a ", listOf("a")),
            arrayOf(" a   b ", listOf("a", "b")),
            arrayOf("a b", listOf("a", "b")),
            arrayOf("a b \"c d\"", listOf("a", "b", "c d"))
    )

    @Test(dataProvider = "provideSplitAsArguments")
    fun `splitAsArguments sunshine`(given: String, expected: List<String>) {
        given.splitAsArguments() shouldMatchValue expected
    }

    fun `StringBuilder plusAssign`() {
        val sb = StringBuilder()
        sb += 'c'
        sb.toString() shouldMatchValue "c"
    }

    fun `containsAll - sunshine`() {
        "ab".containsAll("a") shouldMatchValue true
        "ab".containsAll("a", "b") shouldMatchValue true
        "ab".containsAll("a", "b", "c") shouldMatchValue false

        "a".containsAll("A", ignoreCase = false) shouldMatchValue false
        "a".containsAll("A", ignoreCase = true) shouldMatchValue true
    }

    @DataProvider
    fun provideCutOffStringWithDefaultSymbol(): Array<Array<out Any?>> = arrayOf(
            arrayOf("", 0, ""),
            arrayOf("a", 10, "a"),
            arrayOf("123456789", 1, "1"),
            arrayOf("123456789", 2, "12"),
            arrayOf("123456789", 3, "123"),
            arrayOf("123456789", 4, "1234"),
            arrayOf("123456789", 5, "1 ..."),
            arrayOf("123456789", 6, "12 ...")
    )

    @Test(dataProvider = "provideCutOffStringWithDefaultSymbol")
    fun `cutOff - default symbol`(givenString: String, length: Int, expected: String) {
        assertThat(givenString.cutOffAt(length), equalTo(expected))
    }

    @DataProvider
    fun provideCutOffStringWithCustomSymbol(): Array<Array<out Any?>> = arrayOf(
            arrayOf("", 0, "x", ""),
            arrayOf("a", 10, "x", "a"),
            arrayOf("123456789", 1, "x", "1"),
            arrayOf("123456789", 2, "x", "1x"),
            arrayOf("123456789", 3, "x", "12x")
    )

    @Test(dataProvider = "provideCutOffStringWithCustomSymbol")
    fun `cutOff - custom symbol`(givenString: String, length: Int, cutOffSymbol: String, expected: String) {
        assertThat(givenString.cutOffAt(length, cutOffSymbol), equalTo(expected))
    }

}
