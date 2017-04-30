package com.github.christophpickl.kpotpourri.markdown4k.internal

internal object MarkdownParser {

    private val snippetStart = "```kotlin"
    private val snippetEnd = "```"

    internal fun extractKotlinCode(mdContent: String): List<CodeAndLineNumber> {
        var kotlinFlag = false
        val result = mutableListOf<CodeAndLineNumber>()
        var tempLines = StringBuilder()
        var foundLineNumber = -1
        mdContent.lines().forEachIndexed { lineNumber, line ->
            if (line == snippetStart) {
                // lineNumber index is 0-based, whereas we want human readable 1-based
                foundLineNumber = lineNumber + 1
                kotlinFlag = true
            } else if (line == snippetEnd && kotlinFlag) {
                kotlinFlag = false
                result += CodeAndLineNumber(foundLineNumber, tempLines.toString())
                tempLines = StringBuilder()
            } else if (kotlinFlag) {
                tempLines.appendln(line)
            }
        }
        return result
    }

}

/**
 * Avoid non-informative Pair<String, Int> only :)
 */
internal data class CodeAndLineNumber(
        val lineNumber: Int,
        val code: String
)
