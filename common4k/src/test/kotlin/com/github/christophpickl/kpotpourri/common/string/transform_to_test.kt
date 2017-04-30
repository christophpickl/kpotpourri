package com.github.christophpickl.kpotpourri.common.string

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.nullValue
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class TransformToTest {

    @DataProvider
    fun provideValid(): Array<Array<out Any>> = arrayOf(
            arrayOf("1", true),
            arrayOf("y", true),
            arrayOf("Y", true),
            arrayOf("yes", true),
            arrayOf("Yes", true),
            arrayOf("YES", true),
            arrayOf("on", true),
            arrayOf("ON", true),

            arrayOf("0", false),
            arrayOf("n", false),
            arrayOf("N", false),
            arrayOf("no", false),
            arrayOf("No", false),
            arrayOf("NO", false),
            arrayOf("off", false),
            arrayOf("off", false)
    )

    @Test(dataProvider = "provideValid")
    fun `toBooleanLenient2 and toBooleanLenient2OrNull provide valid`(input: String, expected: Boolean) {
        assertThat(input.toBooleanLenient2(), equalTo(expected))
        assertThat(input.toBooleanLenient2OrNull(), equalTo(expected))
    }

    @DataProvider
    fun provideInvalid(): Array<Array<out Any>> = arrayOf(
            arrayOf(""),
            arrayOf(" "),
            arrayOf("a"),
            arrayOf("2"),
            arrayOf("ja")
    )

    @Test(dataProvider = "provideInvalid")
    fun `toBooleanLenient2 and toBooleanLenient2OrNull provide invalid`(input: String) {
        assertThrown<KPotpourriException> {
            input.toBooleanLenient2()
        }
        assertThat(input.toBooleanLenient2OrNull(), nullValue())
    }

}
