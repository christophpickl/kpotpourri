package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsExactlyInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test
import java.io.File

@Test
class Markdown4kTest {

    private val resources = "src/test/resources"

    fun `kollect - Sunshine integration test`() {
        assertThat(Markdown4k.kollect(File("$resources/dir1")),
                containsExactlyInAnyOrder(CodeSnippet(
                        relativePath = "/file1.md",
                        markdown = File("$resources/dir1/file1.md"),
                        lineNumber = 5,
                        code = "var x = 2\n"
                )))

    }

    fun `kompile - Given unsafed instruction, Then ignores uncompilable code and dont throw exception`() {
        assertKompileIgnored(CodeSnippet.testee.copy(code =
        """/// ignore
this wont be compiled ;)
"""))
    }

    fun `kompile - Given valid kotlin code, Then compiles successfully`() {
        assertKompileSuccess(CodeSnippet.testee.copy(code =
        """
println("hello markdown4k")
"""))
    }

}
