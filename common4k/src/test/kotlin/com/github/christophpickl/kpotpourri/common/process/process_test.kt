package com.github.christophpickl.kpotpourri.common.process

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.io.Io
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test
import java.io.File
import java.io.IOException

@Test class ProcessExecuterImplTest {

    private val cwd = File(".")

    fun `execute - echo with argument foo, Should stdout containt echo and foo`() {
        val stdout = Io.readFromStdOut {
            ProcessExecuterImpl().execute("echo", "ProcessExecuterImplTest", cwd)
        }
        assertThat(stdout, containsSubstrings("echo", "ProcessExecuterImplTest"))
    }

    fun `execute - when output suppressed, Then should be empty`() {
        val stdout = Io.readFromStdOut {
            ProcessExecuterImpl().execute("echo", "foo", cwd, suppressOutput = true)
        }
        stdout shouldMatchValue ""
    }

    fun `execute - invalid command should fail`() {
        assertThrown<IOException>(expectedMessageParts = listOf("not_valid")) {
            ProcessExecuterImpl().execute("not_valid", "", cwd)
        }
    }

    fun `execute - command which fails should fail`() {
        assertThrown<KPotpourriException>(expectedMessageParts = listOf("ProcessExecuterImplTest")) {
            ProcessExecuterImpl().execute("ls", "ProcessExecuterImplTest", cwd)
        }
    }

}
