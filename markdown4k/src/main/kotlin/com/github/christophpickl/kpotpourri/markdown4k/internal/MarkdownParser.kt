package com.github.christophpickl.kpotpourri.markdown4k.internal

/**
 * Extract kotlin code snippet from given text.
 */
internal object MarkdownParser {

    private val snippetStart = "```kotlin"
    private val snippetEnd = "```"

    // MINOR this part could be refactored a bit
    internal fun extractKotlinCode(mdContent: String): List<CodeAndLineNumber> {
        var kotlinFlag = false
        val result = mutableListOf<CodeAndLineNumber>()
        var tempLines = StringBuilder()
        var lineWhereSnippetStarted = -1
        mdContent.lines().forEachIndexed { lineNumber, line ->
            if (line == snippetStart) {
                // lineNumber index is 0-based, whereas we want human readable 1-based
                lineWhereSnippetStarted = lineNumber + 1
                kotlinFlag = true
            } else if (line == snippetEnd && kotlinFlag) {
                kotlinFlag = false
                result += CodeAndLineNumber(lineWhereSnippetStarted, tempLines.toString())
                tempLines = StringBuilder()
            } else if (kotlinFlag) {
                tempLines.appendln(line)
            }
        }
        return result
    }

}

/**
 * Avoid non-informative Pair<String, Int> by defining an own type.
 */
internal data class CodeAndLineNumber(
        val lineNumber: Int,
        val code: String
)
