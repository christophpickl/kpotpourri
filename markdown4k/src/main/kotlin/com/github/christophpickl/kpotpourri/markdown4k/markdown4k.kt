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

// FIXME get rid of warning about Kotlin runtime library

object Markdown4k {

    fun collectSnippets(
            root: File,
            ignoreFolders: List<String> = emptyList()
    ): Array<Array<out Any>> {
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
        return result.map { arrayOf(it) }.toTypedArray()
    }

    /**
     * @throws ScriptException if code is not compileable
     */
    fun compile(snippet: CodeSnippet) {
        if (snippet.isMarkedAsUnsafeCode) {
            log.debug { "Skipping Kotlin snippet as it is marked as unsafe: $snippet" }
            return
        }
        KotlinCompiler.compile(snippet.code)
    }

}

data class CodeSnippet(
        val relativePath: String,
        val markdown: File,
        val lineNumber: Int,
        val code: String
) {
    companion object {
        private val unsafeEscapeSequence = "/// unsafe"
    }

    val isMarkedAsUnsafeCode = code.startsWith(unsafeEscapeSequence)

    override fun toString() = MoreObjects.toStringHelper(this)
            .add("relativePath", relativePath)
            .add("lineNumber", lineNumber)
            .add("code", code.substring(0, Math.min(code.length, 10))) // TODO add in common4k to cut with " ..." at the end
            .toString()
}
