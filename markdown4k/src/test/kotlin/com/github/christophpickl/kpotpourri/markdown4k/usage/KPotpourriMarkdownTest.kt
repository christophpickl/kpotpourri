package com.github.christophpickl.kpotpourri.markdown4k.usage

import com.github.christophpickl.kpotpourri.markdown4k.CodeSnippet
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
import com.github.christophpickl.kpotpourri.markdown4k.assertKompileSuccessOrIgnored
import com.github.christophpickl.kpotpourri.test4k.toDataProviding
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

/**
 * Checks all MD files in KPotopurri sourcefolder for compiling Kotlin code snippets.
 */
@Test class KPotpourriMarkdownTest {

    @DataProvider
    fun provideSnippets() = Markdown4k.kollect(
            root = File("../"),
            ignoreFolders = listOf("src", "build", ".git")
    ).toDataProviding()

    @Test(dataProvider = "provideSnippets")
    fun `markdown`(snippet: CodeSnippet) {
        assertKompileSuccessOrIgnored(snippet)
    }

}
