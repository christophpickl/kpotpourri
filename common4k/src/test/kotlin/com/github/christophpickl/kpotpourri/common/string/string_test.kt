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

    fun `saveToFile - sunshine`() {
        val temp = File.createTempFile("temp", ".txt")
        "some content".saveToFile(temp)

        assertThat(temp.readText(), equalTo("some content"))
    }

    fun `containsAll - sunshine`() {
        "ab".containsAll("a") shouldMatchValue true
        "ab".containsAll("a", "b") shouldMatchValue true
        "ab".containsAll("a", "b", "c") shouldMatchValue false

        "a".containsAll("A", ignoreCase = false) shouldMatchValue false
        "a".containsAll("A", ignoreCase = true) shouldMatchValue true
    }

}
