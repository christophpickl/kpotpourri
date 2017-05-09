package com.github.christophpickl.kpotpourri.markdown4k.non_test

import com.github.christophpickl.kpotpourri.markdown4k.CodeSnippet
import java.io.File

@Suppress("unused")
val CodeSnippet.Companion.testee get() = CodeSnippet(
        relativePath = "relativePath",
        markdown = File("markdown.md"),
        lineNumber = 42,
        code = "println(\"hello world\")"
)
