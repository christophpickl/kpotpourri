@file:Suppress("unused")

package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.io.Io
import com.github.christophpickl.kpotpourri.release4k.Version.*
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

val Version.Companion.testVersion_1 get() = VersionParts1(VersionType.Release, 1)
val Version.Companion.testVersion_2 get() = VersionParts1(VersionType.Release, 2)
val Version.Companion.testVersion_1_2 get() = VersionParts2(VersionType.Release, 1, 2)
val Version.Companion.testVersion_1_2_3 get() = VersionParts3(VersionType.Release, 1, 2, 3)
val Version.Companion.testVersion_1_2_3_4 get() = VersionParts4(VersionType.Release, 1, 2, 3, 4)

@Test class VersionTest {

    private val anyType = VersionType.Release

    fun `increment - version1`() {
        version1(1).increment1() shouldMatchValue version1(2)
    }

    fun `increment - version2`() {
        version2(1, 1).increment1() shouldMatchValue version2(2, 1)
        version2(1, 1).increment2() shouldMatchValue version2(1, 2)
    }

    fun `increment - version3`() {
        version3(1, 1, 1).increment1() shouldMatchValue version3(2, 1, 1)
        version3(1, 1, 1).increment2() shouldMatchValue version3(1, 2, 1)
        version3(1, 1, 1).increment3() shouldMatchValue version3(1, 1, 2)
    }

    fun `increment - version4`() {
        version4(1, 1, 1, 1).increment1() shouldMatchValue version4(2, 1, 1, 1)
        version4(1, 1, 1, 1).increment2() shouldMatchValue version4(1, 2, 1, 1)
        version4(1, 1, 1, 1).increment3() shouldMatchValue version4(1, 1, 2, 1)
        version4(1, 1, 1, 1).increment4() shouldMatchValue version4(1, 1, 1, 2)
    }


    @DataProvider
    fun provideVersionSampleAndReadExecution(): Array<Array<out Any>> = arrayOf(
            arrayOf(Version.testVersion_1, { VersionParts1.readVersion1FromStdin() } as Any, { defVer: Any -> VersionParts1.readVersion1FromStdin(defaultVersion = defVer as VersionParts1) }),
            arrayOf(Version.testVersion_1_2, { VersionParts2.readVersion2FromStdin() } as Any, { defVer: Any -> VersionParts2.readVersion2FromStdin(defaultVersion = defVer as VersionParts2) }),
            arrayOf(Version.testVersion_1_2_3, { VersionParts3.readVersion3FromStdin() } as Any, { defVer: Any -> VersionParts3.readVersion3FromStdin(defaultVersion = defVer as VersionParts3) }),
            arrayOf(Version.testVersion_1_2_3_4, { VersionParts4.readVersion4FromStdin() } as Any, { defVer: Any -> VersionParts4.readVersion4FromStdin(defaultVersion = defVer as VersionParts4) })
    )

    @Test(dataProvider = "provideVersionSampleAndReadExecution")
    fun `read versionX - When type X, Then Version vX is returned`(version: Version, readVersion: () -> Any, @Suppress("UNUSED_PARAMETER") ignoreDefault: (Any) -> Any) {
        Io.readStdoutAndWriteStdin(version.niceString) {
            readVersion() shouldMatchValue version
        }
    }

    @Test(dataProvider = "provideVersionSampleAndReadExecution")
    fun `read versionX - When invalid and then valid entered, Then output contains invalid input`(anyValid: Version, readVersion: () -> Any, @Suppress("UNUSED_PARAMETER") ignoreDefault: (Any) -> Any) {
        val invalidInput = "invalidTestInput"
        // any correct version nr is ok
        val stdout = Io.readStdoutAndWriteStdin("$invalidInput\n${anyValid.niceString}") {
            readVersion() // dont check for return value as already done in other test
        }
        assertThat(stdout, containsSubstrings(invalidInput))
    }

    @Test(dataProvider = "provideVersionSampleAndReadExecution")
    fun `read versionX - Given default version, When hit enter, Then default version is returned and output contains default`(defaultVersion: Version, @Suppress("UNUSED_PARAMETER") ignore: () -> Any, readWithDefault: (Any) -> Any) {
        val stdout = Io.hitEnterAndReadStdout {
            readWithDefault(defaultVersion) shouldMatchValue defaultVersion
        }
        assertThat(stdout, containsSubstrings(defaultVersion.niceString))
    }

    @DataProvider
    fun provideCommonParseVersions(): Array<Array<out Any?>> = arrayOf(
            arrayOf(""),
            arrayOf("a"),
            arrayOf("."),
            arrayOf("1."),
            arrayOf(".1")
    )

    private val parseVersionAnys = listOf(::parseVersion1, ::parseVersion2, ::parseVersion3, ::parseVersion4)
    @Test(dataProvider = "provideCommonParseVersions")
    fun `parse version commons`(failingInput: String) {
        parseVersionAnys.forEach { parseVersion ->
            parseVersion(failingInput) shouldMatchValue null
        }
    }

    fun `parseVersion1 sunshine`() {
        parseVersion1("1") shouldMatchValue Version.testVersion_1
    }

    fun `parseVersion2 sunshine`() {
        parseVersion2("1.2") shouldMatchValue Version.testVersion_1_2
    }

    fun `parseVersion3 sunshine`() {
        parseVersion3("1.2.3") shouldMatchValue Version.testVersion_1_2_3
    }

    fun `parseVersion4 sunshine`() {
        parseVersion4("1.2.3.4") shouldMatchValue Version.testVersion_1_2_3_4
    }

    private fun version1(number1: Int) = VersionParts1(anyType, number1)
    private fun version2(number1: Int, number2: Int) = VersionParts2(anyType, number1, number2)
    private fun version3(number1: Int, number2: Int, number3: Int) = VersionParts3(anyType, number1, number2, number3)
    private fun version4(number1: Int, number2: Int, number3: Int, number4: Int) = VersionParts4(anyType, number1, number2, number3, number4)

}
