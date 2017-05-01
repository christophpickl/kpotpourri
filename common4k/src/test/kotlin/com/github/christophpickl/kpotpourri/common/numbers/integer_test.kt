package com.github.christophpickl.kpotpourri.common.numbers

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class IntegerTest {

    private val any = 0

    @DataProvider
    fun provideBetweens(): Array<Array<out Any>> = arrayOf(
            arrayOf(1, 0, 1, true),
            arrayOf(1, 1, 1, true),
            arrayOf(1, 1, 2, true),
            arrayOf(1, 2, 2, false)
    )

    @Test(dataProvider = "provideBetweens")
    fun `isBetweenInclusive - valid`(pivot: Int, lower: Int, upper: Int, expected: Boolean) {
        assertThat(pivot.isBetweenInclusive(lower, upper), equalTo(expected))
    }

    fun `isBetweenInclusive invalid`() {
        assertThrown<IllegalArgumentException> {
            any.isBetweenInclusive(2, 1)
        }
    }

    fun `forEach - sunshine`() {
        var count = 0
        1.forEach { count++ }
        assertThat(count, equalTo(1))
    }

    fun `forEach - zero invalid`() {
        assertThrown<IllegalArgumentException> {
            0.forEach { }
        }
    }

    fun `forEach - negativ invalid`() {
        assertThrown<IllegalArgumentException> {
            (-1).forEach { }
        }
    }

}
