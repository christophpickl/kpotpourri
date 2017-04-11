package com.github.christophpickl.kpotpourri.common.string

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

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
            arrayOf("http://host.at", "endpoint", "http://host.at/endpoint"),
            arrayOf("http://host.at/", "endpoint", "http://host.at/endpoint"),
            arrayOf("http://host.at", "/endpoint", "http://host.at/endpoint"),
            arrayOf("http://host.at/", "/endpoint", "http://host.at/endpoint"),
            arrayOf("http://host.at/", "/endpoint/", "http://host.at/endpoint/")
    )

    @Test(dataProvider = "provideConcatUrls")
    fun `concat urls`(part1: String, part2: String, expected: String) {
        assertThat(concatUrlParts(part1, part2), equalTo(expected))
        // aliase
        assertThat(joinUrlParts(part1, part2), equalTo(expected))
        assertThat(combineUrlParts(part1, part2), equalTo(expected))
    }
}
