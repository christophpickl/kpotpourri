package com.github.christophpickl.kpotpourri.markdown4k

@Suppress("unused")
val CodeSnippet.Companion.testee get() = CodeSnippet(
        relativePath = "relativePath",
        markdown = java.io.File("markdown.md"),
        lineNumber = 42,
        code = "println(\"hello world\")"
)
