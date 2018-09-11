package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.string.cutOffAt
import com.google.common.base.MoreObjects
import java.io.File
import javax.script.ScriptException

/**
 * Possible result of compiling a Kotlin code snippet.
 */
sealed class KompilationResult {

    /** Code was compilable. */
    object Success : KompilationResult()

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
