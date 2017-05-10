package com.github.christophpickl.kpotpourri.markdown4k.non_test

import com.github.christophpickl.kpotpourri.markdown4k.CodeSnippet
import com.github.christophpickl.kpotpourri.markdown4k.KompilationResult
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
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
    assertThat(Markdown4k.kompile(code), anyOf(
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
