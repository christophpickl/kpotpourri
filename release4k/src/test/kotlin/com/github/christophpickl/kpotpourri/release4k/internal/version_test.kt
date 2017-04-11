package com.github.christophpickl.kpotpourri.release4k.internal

import com.github.christophpickl.kpotpourri.release4k.Version
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class VersionReaderTest {

    @DataProvider
    fun provideValidVersions(): Array<Array<out Any>> = arrayOf(
            arrayOf("1", listOf(1)),
            arrayOf(" 1", listOf(1)),
            arrayOf("1 ", listOf(1)),
            arrayOf("1.2", listOf(1, 2)),
            arrayOf(" 1 .  2 ", listOf(1, 2)),
            arrayOf("1.2.3", listOf(1, 2, 3)),
            arrayOf("1.2.3.4", listOf(1, 2, 3, 4))
    )

    @Test(dataProvider = "provideValidVersions")
    fun `valid versions read`(toBeRead: String, numbers: List<Int>) {
        assertRead(toBeRead, VersionReader.buildVersion(numbers)!!)
    }

    @DataProvider
    fun provideInvalidVersions(): Array<Array<out Any>> = arrayOf(
            arrayOf(""),
            arrayOf("a"),
            arrayOf("1a"),
            arrayOf("a1"),
            arrayOf("1."),
            arrayOf(".1"),
            arrayOf("1.a"),
            arrayOf("1.1a"),
            arrayOf("a.1")
    )

    @Test(dataProvider = "provideInvalidVersions")
    fun `invalid versions throw`(toBeRead: String) {
        assertThrown<VersionParseException> {
            VersionReader.read(toBeRead)
        }
    }

    private fun assertRead(toBeRead: String, expected: Version) {
        assertThat(VersionReader.read(toBeRead),
                equalTo(expected))
    }

}
