package com.github.christophpickl.kpotpourri.common.numbers

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File.separator

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

}
