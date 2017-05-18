package com.github.christophpickl.kpotpourri.release4k.internal

import com.github.christophpickl.kpotpourri.common.io.Io
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test
import java.io.File

@Test class MiscTest {

    private val message = "testMessage"

    fun `kout prints prefix`() {
        val actual = Io.readFromStdOut {
            kout(message)
        }

        assertThat(actual, equalTo("[KRELEASE] $message\n"))
    }

    fun `koutFile prints path and prefix`() {
        val file = File("")
        val actual = Io.readFromStdOut {
            koutFile(file, message)
        }

        assertThat(actual, equalTo("[${file.canonicalPath}] $message\n"))
    }

}
