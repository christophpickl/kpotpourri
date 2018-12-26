package com.github.christophpickl.kpotpourri.build

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
class VersionKtTest {

    @DataProvider
    fun provideVersion1() = arrayOf(
        arrayOf("", null),
        arrayOf("a", null),
        arrayOf("1.1", null),
        arrayOf("1-SNAPSHOT", null),
        arrayOf("0", Version1(0)),
        arrayOf("1", Version1(1)),
        arrayOf("12", Version1(12))
    )

    @Test(dataProvider = "provideVersion1")
    fun `Parse version1`(input: String, expected: Version1?) {
        assertThat(Version1.parse(input)).isEqualTo(expected)
    }

    @DataProvider
    fun provideVersion2() = arrayOf(
        arrayOf("", null),
        arrayOf("1", null),
        arrayOf("1.2", Version2(1, 2)),
        arrayOf("12.34", Version2(12, 34))
    )

    @Test(dataProvider = "provideVersion2")
    fun `Parse version2`(input: String, expected: Version2?) {
        assertThat(Version2.parse(input)).isEqualTo(expected)
    }

    @DataProvider
    fun provideVersion3() = arrayOf(
        arrayOf("", null),
        arrayOf("1", null),
        arrayOf("1.2", null),
        arrayOf("1.2.3", Version3(1, 2, 3)),
        arrayOf("12.34.56", Version3(12, 34, 56))
    )

    @Test(dataProvider = "provideVersion3")
    fun `Parse version3`(input: String, expected: Version3?) {
        assertThat(Version3.parse(input)).isEqualTo(expected)
    }

    @DataProvider
    fun provideVersion4() = arrayOf(
        arrayOf("", null),
        arrayOf("1", null),
        arrayOf("1.2", null),
        arrayOf("1.2.3", null),
        arrayOf("1.2.3.4", Version4(1, 2, 3, 4)),
        arrayOf("12.34.56.78", Version4(12, 34, 56, 78))
    )

    @Test(dataProvider = "provideVersion4")
    fun `Parse version4`(input: String, expected: Version4?) {
        assertThat(Version4.parse(input)).isEqualTo(expected)
    }
}
