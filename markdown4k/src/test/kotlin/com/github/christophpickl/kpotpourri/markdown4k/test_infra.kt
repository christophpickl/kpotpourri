package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.enforceAllBranchesCovered
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.anyOf
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.isA
import com.github.christophpickl.kpotpourri.test4k.toDataProviding
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File
import kotlin.reflect.KClass

@Test
abstract class MarkdownTestngTest(val root: File, val ignoreFolders: List<String> = emptyList()) {

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

@Suppress("unused")
val CodeSnippet.Companion.testee get() = CodeSnippet(
        relativePath = "relativePath",
        markdown = File("markdown.md"),
        lineNumber = 42,
        code = "println(\"hello world\")"
)

fun assertKompileSuccessOrIgnored(code: CodeSnippet) {
    val actual = Markdown4k.kompile(code)
    assertThat("Actual: $actual", actual, anyOf(
            isA(KompilationResult.Success::class),
            isA(KompilationResult.Ignored::class)
    ))
}

fun assertKompileSuccess(code: CodeSnippet) {
    assertKompileAny(code, KompilationResult.Success::class)
}

fun assertKompileFailure(code: CodeSnippet) {
    assertKompileAny(code, KompilationResult.Failure::class)
}

fun assertKompileIgnored(code: CodeSnippet) {
    assertKompileAny(code, KompilationResult.Ignored::class)
}

private fun assertKompileAny(code: CodeSnippet, expected: KClass<out KompilationResult>) {
    assertThat(Markdown4k.kompile(code), isA(expected))
}
