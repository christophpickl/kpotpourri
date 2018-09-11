package com.github.christophpickl.kpotpourri.markdown4k.internal

/**
 * Extract kotlin code snippet from given text.
 */
internal object MarkdownParser {

    private val snippetStart = "```kotlin"
    private val snippetEnd = "```"

    internal fun extractKotlinCode(mdContent: String): List<LineNumberAndCode> {
        var kotlinFlag = false
        val result = mutableListOf<LineNumberAndCode>()
        var tempLines = StringBuilder()
        var lineWhereSnippetStarted = -1
        mdContent.lines().forEachIndexed { lineNumber, line ->
            if (line == snippetStart) {
                // lineNumber index is 0-based, whereas we want human readable 1-based
                lineWhereSnippetStarted = lineNumber + 1
                kotlinFlag = true
            } else if (line == snippetEnd && kotlinFlag) {
                kotlinFlag = false
                result += LineNumberAndCode(
                        code = tempLines.toString(),
                        lineNumber = lineWhereSnippetStarted
                )
                tempLines = StringBuilder()
            } else if (kotlinFlag) {
                tempLines.appendln(line)
            }
        }
        return result
    }

}

/**
 * Define own type instead of having a generic Pair.
 */
internal data class LineNumberAndCode(
        val lineNumber: Int,
        val code: String
)
