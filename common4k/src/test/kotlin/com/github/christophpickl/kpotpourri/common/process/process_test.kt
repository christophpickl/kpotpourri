package com.github.christophpickl.kpotpourri.common.process

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.io.Io
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import org.testng.annotations.Test
import java.io.IOException

@Test class ProcessExecuterImplTest {

    fun `execute - echo with argument foo, Should stdout containt echo and foo`() {
        val stdout = Io.readFromStdOut {
            val returnCode = ProcessExecuterImpl.execute("echo", listOf("ProcessExecuterImplTest"))
            assertThat(returnCode, equalTo(0))
        }
        assertThat(stdout, containsSubstrings("echo", "ProcessExecuterImplTest"))
    }

    fun `execute - when output suppressed, Then should be empty`() {
        val stdout = Io.readFromStdOut {
            val returnCode = ProcessExecuterImpl.execute("echo", listOf("foo"), ExecuteContext(suppressOutput = true))
            assertThat(returnCode, equalTo(0))
        }
        
        val stdoutWithoutLog = stdout.lines().filter { !it.contains("[TESTLOG]") && !it.isEmpty() }
        assertThat(stdoutWithoutLog, isEmpty)
    }

    fun `execute - invalid command should fail`() {
        assertThrown<IOException>(expectedMessageParts = listOf("not_valid")) {
            ProcessExecuterImpl.executeOrThrow("not_valid", listOf(""))
        }
    }

    fun `execute - command which fails should fail`() {
        assertThrown<KPotpourriException>(expectedMessageParts = listOf("ProcessExecuterImplTest")) {
            ProcessExecuterImpl.executeOrThrow("ls", listOf("ProcessExecuterImplTest"))
        }
    }

}
