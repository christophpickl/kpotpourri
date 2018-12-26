package com.github.christophpickl.kpotpourri.build

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import java.io.File

@Test
class Build4kTest {

    fun `readFromFile for all versions`() {
        assertReadFromFile("version1.txt", Version1(1))
        assertReadFromFile("version2.txt", Version2(1, 2))
        assertReadFromFile("version3.txt", Version3(1, 2, 3))
        assertReadFromFile("version4.txt", Version4(1, 2, 3, 4))
    }

    private inline fun <reified V : Version<V>> assertReadFromFile(fileName: String, expected: V) {
        assertThat(Build4k().readFromFile<V>(File("src/test/resources/build4k/$fileName"))).isEqualTo(expected)
    }

}
