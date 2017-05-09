package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.file.nameStartingFrom
import com.github.christophpickl.kpotpourri.common.file.scanForFilesRecursively
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.markdown4k.internal.KotlinCompiler
import com.github.christophpickl.kpotpourri.markdown4k.internal.MarkdownParser
import com.google.common.base.MoreObjects
import java.io.File
import javax.script.ScriptException


/**
 * Collects and verifies Kotlin code snippets within Markdown files.
 */
object Markdown4k {

    private val log = LOG {}

    /**
     * Scan the given directory recursively for MD files containing Kotlin code snippets.
     *
     * @param ignoreFolders specify list of directory names which should be skipped in recursive file scan.
     */
    fun collectSnippets(
            root: File,
            ignoreFolders: List<String> = emptyList()
    ): List<CodeSnippet> {
        log.debug { "collectSnippets(root=${root.canonicalPath}, ignoreFolders=$ignoreFolders)" }
        val result = mutableListOf<CodeSnippet>()
        root.scanForFilesRecursively("md", ignoreFolders).forEach { file ->
            MarkdownParser.extractKotlinCode(file.readText()).forEach { (lineNumber, code) ->
                result += CodeSnippet(
                        markdown = file,
                        relativePath = file.nameStartingFrom(root),
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
        /** File which contains the code snippet. */
        val markdown: File,
        /** Path parth relative to the root scan folder. */
        val relativePath: String,
        val lineNumber: Int,
        val code: String
) {
    companion object {
        private val unsafeEscapeSequence = "/// unsafe"
    }

    internal val isMarkedAsUnsafeCode = code.startsWith(unsafeEscapeSequence)

    /**
     * Used by TestNG and JUnit to construct the test name as it is used by a data provider, so watch out.
     */
    override fun toString() = MoreObjects.toStringHelper(this)
            .add("relativePath", relativePath)
            .add("lineNumber", lineNumber)
            .add("code", code.substring(0, Math.min(code.length, 10))) // MINOR add in common4k to cut with " ..." at the end
            .toString()
}
