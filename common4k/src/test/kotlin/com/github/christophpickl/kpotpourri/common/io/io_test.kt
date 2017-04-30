package com.github.christophpickl.kpotpourri.common.io


import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class IoTest {

    fun `readFromStdOut - When print a string, Then that string should have been captured`() {
        val sentToSysOut = Io.readFromStdOut {
            print("out")
        }
        assertThat(sentToSysOut, equalTo("out"))
    }

    fun `writeToStdIn - When a string is written to stdin, Then that string should have been read`() {
        var actual: String? = null
        Io.writeToStdIn("in") {
            actual = readLine()
        }

        assertThat(actual, equalTo("in"))
    }

    fun `readStdoutAndWriteStdin - sunshine`() {
        var actualIn: String? = null
        val actualOut = Io.readStdoutAndWriteStdin("in") {
            actualIn = readLine()
            print("out")
        }

        assertThat(actualIn, equalTo("in"))
        assertThat(actualOut, equalTo("out"))
    }

    fun `hitEnterAndReadStdout - sunshine`() {
        var actualIn: String? = null
        val actualOut = Io.hitEnterAndReadStdout {
            actualIn = readLine()
            print("out")
        }

        assertThat(actualIn, equalTo(""))
        assertThat(actualOut, equalTo("out"))
    }

}
