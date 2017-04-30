package com.github.christophpickl.kpotpourri.common.time

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class MsTimificationTest {

    @DataProvider
    fun provideSeconds(): Array<Array<out Any>> = arrayOf(
            arrayOf(0L, "0.000 seconds"),
            arrayOf(1L, "0.001 seconds"),
            arrayOf(1000L, "1.000 seconds"),
            arrayOf(1234L, "1.234 seconds"),
            arrayOf(1999L, "1.999 seconds"),
            arrayOf(12345L, "12.345 seconds")
    )

    @Test(dataProvider = "provideSeconds")
    fun `timify seconds`(ms: Long, expected: String) {
        ms.timify(MsTimification.Seconds) shouldMatchValue expected
    }

    @DataProvider
    fun provideMinutes(): Array<Array<out Any>> = arrayOf(
            arrayOf(0L, "0 minutes and 0 seconds"),
            arrayOf(1L, "0 minutes and 0 seconds"),
            arrayOf(1000L, "0 minutes and 1 second"),
            arrayOf(2000L, "0 minutes and 2 seconds"),
            arrayOf(1000L * 60, "1 minute and 0 seconds"),
            arrayOf(1000L * 61, "1 minute and 1 second"),
            arrayOf(1000L * 60 * 42, "42 minutes and 0 seconds")
    )

    @Test(dataProvider = "provideMinutes")
    fun `timify minutes`(ms: Long, expected: String) {
        ms.timify(MsTimification.MinutesAndSeconds) shouldMatchValue expected
    }

}
