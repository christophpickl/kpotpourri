package com.github.christophpickl.kpotpourri.markdown4k.non_test

import com.github.christophpickl.kpotpourri.markdown4k.CodeSnippet
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
import com.github.christophpickl.kpotpourri.test4k.toDataProviding
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

/**
 * Checks all MD files in KPotopurri sourcefolder for compiling Kotlin code snippets.
 */
@Test class KPotpourriMarkdownTest {

    @DataProvider
    fun provideSnippets() = Markdown4k.collectSnippets(
            root = File("../"),
            ignoreFolders = listOf("src", "build", ".git")
    ).toDataProviding()

    @Test(dataProvider = "provideSnippets")
    fun `compiling markdown should not throw exception`(snippet: CodeSnippet) {
        Markdown4k.compile(snippet)
    }

}
