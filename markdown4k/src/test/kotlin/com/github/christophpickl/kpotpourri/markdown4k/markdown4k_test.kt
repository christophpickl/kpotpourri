package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsExactlyInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test
import java.io.File

@Test class Markdown4kTest {

    fun `collectSnippets - Sunshine integration test`() {
        assertThat(Markdown4k.collectSnippets(File("src/test/resources/dir1")),
                containsExactlyInAnyOrder(CodeSnippet(
                        relativePath = "/dir1/file1.md",
                        markdown = File("file1.md"),
                        lineNumber = 5,
                        code = "\nvar x = 2\n"
                )))
    }

    fun `compile - Given unsafed instruction, Then ignores uncompilable code`() {
        Markdown4k.compile(CodeSnippet.testee.copy(code =
        """/// unsafe
this wont be compiled ;)
"""))
    }

}
