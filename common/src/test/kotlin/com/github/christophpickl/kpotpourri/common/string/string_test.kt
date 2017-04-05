package com.github.christophpickl.kpotpourri.common.string

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class StringExtensionsTest {

    @DataProvider//(name = "luckyDataProvider")
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
}
