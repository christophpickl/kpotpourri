package com.github.christophpickl.kpotpourri.common.string

import com.github.christophpickl.kpotpourri.common.string.nullIfEmpty
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

}
