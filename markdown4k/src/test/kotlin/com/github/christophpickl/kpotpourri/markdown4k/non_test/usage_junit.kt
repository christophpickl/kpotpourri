package com.github.christophpickl.kpotpourri.markdown4k.non_test

import com.github.christophpickl.kpotpourri.markdown4k.CodeSnippet
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
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
        @Parameterized.Parameters(name = "{index}: compile {0}")
        fun data() =
                Markdown4k.collectSnippets(
                        root = File("../"),
                        ignoreFolders = listOf("src", "build", ".git")
                ).toParamterized()
    }

    @Test
    fun `compiling markdown should not throw exception`() {
        Markdown4k.compile(snippet)
    }

}
