package com.github.christophpickl.kpotpourri.markdown4k.usage

import com.github.christophpickl.kpotpourri.markdown4k.CodeSnippet
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
import com.github.christophpickl.kpotpourri.markdown4k.assertKompileSuccessOrIgnored
import com.github.christophpickl.kpotpourri.test4k.toParamterized
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.File

/**
 * Example of using Markdown4k together with JUnit.
 */
@RunWith(Parameterized::class)
class UsageJUnitTest(private val snippet: CodeSnippet) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: kompile {0}")
        fun data() =
                Markdown4k.kollect(
                        root = File("."), // assume the current working directory contains the MD files
                        ignoreFolders = listOf("src", "build", ".git")
                ).toParamterized()
    }

    @Test
    fun `kompile`() {
        assertKompileSuccessOrIgnored(snippet)
    }

}
