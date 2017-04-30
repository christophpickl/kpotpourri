package com.github.christophpickl.kpotpourri.common.numbers

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class IntegerTest {

    @DataProvider
    fun provideBetweens(): Array<Array<out Any>> = arrayOf(
            arrayOf(1, 0, 1, true),
            arrayOf(1, 1, 1, true),
            arrayOf(1, 1, 2, true),
            arrayOf(1, 2, 2, false)
    )

    @Test(dataProvider = "provideBetweens")
    fun `isBetween`(pivot: Int, lower: Int, upper: Int, expected: Boolean) {
        assertThat(pivot.isBetweenInclusive(lower, upper), equalTo(expected))
    }

    fun `forEach`() {
        var count = 0
        3.forEach { count++ }
        assertThat(count, equalTo(3))
    }
}
