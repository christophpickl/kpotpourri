package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.anyOf
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.isA
import com.natpryce.hamkrest.assertion.assertThat
import java.io.File
import kotlin.reflect.KClass

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
