package com.github.christophpickl.kpotpourri.common.numbers

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class NumbersTest {

    @DataProvider
    fun provideDoubleFormats(): Array<Array<out Any>> = arrayOf(
            arrayOf(1.0, 0, "1"),
            arrayOf(1.0, 1, "1.0"),
            arrayOf(1.11, 1, "1.1"),
            arrayOf(1.14, 1, "1.1"),
            arrayOf(1.15, 1, "1.2"),
            arrayOf(1.19, 1, "1.2"),
            arrayOf(1.2, 2, "1.20")
    )

    @Test(dataProvider = "provideDoubleFormats")
    fun `double format`(number: Double, digits: Int, expected: String) {
        assertThat(number.format(digits), equalTo(expected))
    }

    @DataProvider
    fun provideDoubleToSeconds(): Array<Array<out Any>> = arrayOf(
            arrayOf(1_000, 0, "", "1"),
            arrayOf(1_234, 0, "", "1"),
            arrayOf(1_234, 1, "", "1.2"),
            arrayOf(1_234, 3, "", "1.234"),
            arrayOf(123_456, 3, "", "123.456"),
            arrayOf(1, 0, "", "0"),
            arrayOf(1, 0, "s", "0s"),
            arrayOf(1, 1, "", "0.0"),
            arrayOf(1, 2, "", "0.00")
    )

    @Test(dataProvider = "provideDoubleToSeconds")
    fun `double toSeconds`(number: Double, digits: Int, suffix: String, expected: String) {
        assertThat(number.toSeconds(digits, suffix), equalTo(expected))
    }

    fun `double toSeconds with defaults`() {
        assertThat((1234.5678).toSeconds(), equalTo("1.235 secs"))
    }

}
