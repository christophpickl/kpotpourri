package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.file.nameStartingFrom
import com.github.christophpickl.kpotpourri.common.file.scanForFilesRecursively
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.markdown4k.internal.KotlinCompiler
import com.github.christophpickl.kpotpourri.markdown4k.internal.MarkdownParser
import com.google.common.base.MoreObjects
import java.io.File
import javax.script.ScriptException

private val log = LOG {}

/**
 * Collects and verifies Kotlin code snippets within Markdown files.
 */
object Markdown4k {

    /**
     * Scan the given directory recursively for MD files containing Kotlin code snippets.
     */
    fun collectSnippets(
            root: File,
            ignoreFolders: List<String> = emptyList()
    ): List<CodeSnippet> {
        val result = mutableListOf<CodeSnippet>()
        root.scanForFilesRecursively("md", ignoreFolders).forEach { file ->
            MarkdownParser.extractKotlinCode(file.readText()).forEach { (lineNumber, code) ->
                result += CodeSnippet(
                        relativePath = file.nameStartingFrom(root),
                        markdown = file,
                        lineNumber = lineNumber,
                        code = code
                )
            }
        }
        return result
    }

    /**
     * Suppress compilation by prefixing the code with "/// unsafe".
     *
     * @throws ScriptException if code is not compilable.
     */
    fun compile(snippet: CodeSnippet) {
        if (snippet.isMarkedAsUnsafeCode) {
            log.debug { "Skipping Kotlin snippet as it is marked as unsafe: $snippet" }
            return
        }
        KotlinCompiler.compile(snippet.code)
    }

}

/**
 * A single Kotlin code snippet found in a Markdown file.
 */
data class CodeSnippet(
        val relativePath: String,
        val markdown: File,
        val lineNumber: Int,
        val code: String
) {
    companion object {
        private val unsafeEscapeSequence = "/// unsafe"
    }

    internal val isMarkedAsUnsafeCode = code.startsWith(unsafeEscapeSequence)

    /**
     * Used by TestNG to construct the test name as it is used by a data provider.
     */
    override fun toString() = MoreObjects.toStringHelper(this)
            .add("relativePath", relativePath)
            .add("lineNumber", lineNumber)
            .add("code", code.substring(0, Math.min(code.length, 10))) // MINOR add in common4k to cut with " ..." at the end
            .toString()
}
