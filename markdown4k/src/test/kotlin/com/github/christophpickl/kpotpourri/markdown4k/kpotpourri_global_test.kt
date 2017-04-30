package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.test4k.toDataProviding
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

/**
 * Checks all MD files in KPotopurri sourcefolder for compiling Kotlin code snippets.
 */
@Test class KPotpourriMarkdownKotlinCheckerTest {

    private val root = File("../")
    private val ignoreFolders = listOf("src", "build", ".git")

    @DataProvider
    fun provideSnippets() = Markdown4k.collectSnippets(root, ignoreFolders).toDataProviding()

    @Test(dataProvider = "provideSnippets")
    fun `compilable snippet`(snippet: CodeSnippet) {
        Markdown4k.compile(snippet)
    }

}
