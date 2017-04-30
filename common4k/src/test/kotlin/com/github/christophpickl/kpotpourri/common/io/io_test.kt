package com.github.christophpickl.kpotpourri.common.io


import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class IoTest {

    private val ANY_VALUE = "testValue"

    fun `readFromStdOut - sunshine`() {
        val sentToSysOut = Io.readFromStdOut {
            print(ANY_VALUE)
        }
        assertThat(sentToSysOut, equalTo(ANY_VALUE))
    }

    fun `writeToStdIn - sunshine`() {
        var actual: String? = null
        Io.writeToStdIn(ANY_VALUE) {
            actual = readLine()
        }
        assertThat(actual, equalTo(ANY_VALUE))
    }

    // MINOR test readStdoutAndWriteStdin + hitEnterAndReadStdout

}
