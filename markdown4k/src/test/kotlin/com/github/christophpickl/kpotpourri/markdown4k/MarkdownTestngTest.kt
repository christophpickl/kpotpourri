package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.enforceAllBranchesCovered
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.test4k.toDataProviding
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

@Test
abstract class MarkdownTestngTest(
        val root: File,
        val ignoreFolders: List<String> = emptyList()
) {

    private val log = LOG {}

    init {
        log.debug { "markdown4k root directory: ${root.canonicalPath}" }
    }

    @DataProvider
    fun provideSnippets() =
            Markdown4k.kollect(root, ignoreFolders, suppressIgnoredSnippets = true).toDataProviding()

    @Test(dataProvider = "provideSnippets")
    fun kompile(snippet: CodeSnippet) {
        val kompilationResult = Markdown4k.kompile(snippet)
        when (kompilationResult) {
            is KompilationResult.Success -> {
                // do nothing
            }
            is KompilationResult.Failure -> {
                Assert.fail("Compilation failed for: $snippet", kompilationResult.exception)
            }
            is KompilationResult.Ignored -> {
                throw IllegalStateException("This is actually not possible as suppressIgnoredSnippets was set to true!")
            }
        }.enforceAllBranchesCovered
    }

}
