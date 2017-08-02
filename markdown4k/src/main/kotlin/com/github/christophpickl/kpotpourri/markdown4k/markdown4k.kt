package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.file.nameStartingFrom
import com.github.christophpickl.kpotpourri.common.file.scanForFilesRecursively
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.common.string.cutOffAt
import com.github.christophpickl.kpotpourri.markdown4k.internal.Kompiler
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
    fun kollect(
            root: File,
            ignoreFolders: List<String> = emptyList(),
            suppressIgnoredSnippets: Boolean = true
    ): List<CodeSnippet> {
        log.info { "collectSnippets(root=${root.canonicalPath}, ignoreFolders=$ignoreFolders)" }
        val result = mutableListOf<CodeSnippet>()
        root.scanForFilesRecursively("md", ignoreFolders).forEach { file ->
            MarkdownParser.extractKotlinCode(file.readText()).forEach { (lineNumber, code) ->
                val snippet = CodeSnippet(
                        markdown = file,
                        relativePath = file.nameStartingFrom(root),
                        lineNumber = lineNumber,
                        code = code
                )
                if (!suppressIgnoredSnippets || !snippet.isIgnored) {
                    result += snippet
                }
            }
        }
        return result
    }

    /**
     * Suppress compilation by prefixing the code with "/// ignore".
     */
    fun kompile(snippet: CodeSnippet): KompilationResult {
        if (snippet.isIgnored) {
            log.debug { "Skipping Kotlin snippet as it is marked as unsafe: $snippet" }
            return KompilationResult.Ignored()
        }
        return Kompiler.kompile(snippet.code).toKompilationResult()
    }

}

/**
 * Possible result of compiling a Kotlin code snippet.
 */
sealed class KompilationResult {

    /** Code was compilable. */
    class Success : KompilationResult()

    /**
     * There was a compilation error, details can be found in the provided exception.
     */
    data class Failure(
            /** The underlying exception which has been thrown from the script engine. */
            val exception: ScriptException
    ) : KompilationResult()

    /** The provided code snippet was marked to be unsafe and skipped compilation. */
    class Ignored : KompilationResult()

}

/**
 * A single Kotlin code snippet found in a Markdown file.
 */
data class CodeSnippet(
        /** File which contains the code snippet. */
        val markdown: File,
        /** Path parth relative to the root scan folder. */
        val relativePath: String,
        /** Line number of the ```kotlin declaration. */
        val lineNumber: Int,
        /** The actual kotlin code. */
        val code: String
) {
    companion object {
        private val ignoreEscapeSequence = "/// ignore"
    }

    internal val isIgnored = code.startsWith(ignoreEscapeSequence)

    /**
     * Used by TestNG and JUnit to construct the test name as it is used by a data provider, so watch out.
     */
    override fun toString() = MoreObjects.toStringHelper(this)
            .add("relativePath", relativePath)
            .add("lineNumber", lineNumber)
            .add("code", ">>>${code.cutOffAt(20)}<<<")
            .toString()
}
