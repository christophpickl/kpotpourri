package com.github.christophpickl.kpotpourri.markdown4k

import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

@Test class KPotpourriMarkdownKotlinCheckerTest {

    private val root = File("../")
    private val ignoreFolders = listOf("src", "build", ".git")

    @DataProvider
    fun provideMarkdownKotlinSnippets() =
            collectMarkdownSnippets(root, ignoreFolders).toDataProvider()

    @Test(dataProvider = "provideMarkdownKotlinSnippets")
    fun `compilable snippet`(snippet: MarkdownSnippet) {
        checkCodeIsCompilable(snippet.code)
    }

}
