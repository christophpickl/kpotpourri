package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k.collectSnippets
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k.compile
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

@Test class KPotpourriMarkdownKotlinCheckerTest {

    private val root = File("../")
    private val ignoreFolders = listOf("src", "build", ".git")

    @DataProvider
    fun provideSnippets() = collectSnippets(root, ignoreFolders)

    @Test(dataProvider = "provideSnippets")
    fun `compilable snippet`(snippet: CodeSnippet) {
        compile(snippet)
    }

}
