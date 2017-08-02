package com.github.christophpickl.kpotpourri.common.string

import com.google.common.io.Files
import java.io.File


/**
 * Returns `null` if `isEmpty()` evaluates to true.
 */
fun String.nullIfEmpty() =
        if (isEmpty()) null else this

/**
 * Duplicate this string a couple of times.
 */
fun String.times(count: Int): String {
    val symbol = this
    return StringBuilder().apply {
        0.until(count).forEach { append(symbol) }
    }.toString()
}

/**
 * If condition is met, wrap with left/right symbol.
 */
fun String.wrapIf(condition: Boolean, wrappingLeft: String, wrappingRight: String) =
        if (condition) wrappingLeft + this + wrappingRight else this

/**
 * Delegates to wrapIf(Boolean, String, String).
 */
fun String.wrapParenthesisIf(condition: Boolean) =
        wrapIf(condition, "(", ")")

/**
 * Combination of kotlin's `removePrefix` and `removeSuffix`.
 */
fun String.removePreAndSuffix(search: String) =
        this.removePrefix(search).removeSuffix(search)

/**
 * Very basic HTML tag encapsulation and line break conversion.
 */
fun String.htmlize() =
        "<html>" + this.replace("\n", "<br/>") + "</html>"

/**
 * Save the string to the given file using Guava. No validation at all.
 */
fun String.saveToFile(target: File) {
    Files.write(this, target, Charsets.UTF_8)
}

/**
 * Synonym for concatUrlParts().
 */
fun combineUrlParts(vararg parts: String) = concatUrlParts(*parts)

/**
 * Synonym for concatUrlParts().
 */
fun joinUrlParts(vararg parts: String) = concatUrlParts(*parts)

/**
 * Get sure of leading/trailing slashes.
 */
// or: infix joinUrlParts => url1 joinUrlParts url2
fun concatUrlParts(vararg parts: String): String {
    if (parts.all(String::isEmpty)) {
        return ""
    }
    val parts2 = parts.filter({ it.isNotEmpty() && it != "/" })
    return (if (parts2.first().startsWith("/")) "/" else "") +
            parts2.toList().map { it.removePreAndSuffix("/") }.joinToString("/")
    // +(if(parts2.last().endsWith("/")) "/" else "")
}

/**
 * Splits by whitespace but respects the double quite (") symbol to combine big argument.
 *
 * Example: a b "c d" => { "a", "b", "c d" }
 */
fun String.splitAsArguments(): List<String> {
    if (isBlank()) {
        return emptyList()
    }
    val result = mutableListOf<String>()
    var stringCollect = StringBuilder()
    var quoteOpen = false
    (0..length - 1).forEach { index ->
        val char = this[index]
        if (char == '\"') {
            if (quoteOpen) {
                result += stringCollect.toString()
                stringCollect = StringBuilder()
                quoteOpen = false
            } else {
                quoteOpen = true
            }
        } else if (char == ' ') {
            if (quoteOpen) {
                stringCollect += char
            } else {
                result += stringCollect.toString()
                stringCollect = StringBuilder()
            }
        } else {
            stringCollect += char
        }
    }
    result += stringCollect.toString() // append last
    return result.filter(String::isNotEmpty)
}

/**
 * Kotlin integration for: StringBuilder() += 'k'
 */
operator fun StringBuilder.plusAssign(char: Char) {
    append(char)
}

/**
 * Enhance the `contains` method receiving multiple substrings instead only of a single.
 */
fun String.containsAll(vararg substrings: String, ignoreCase: Boolean = false) =
        substrings.all { this.contains(it, ignoreCase) }

/**
 * Ensure a maximum length but cutting of overlengthy part and append some indication symbol.
 *
 * E.g.: "1234567" cut off by 3 => "123 ..."
 */
fun String.cutOffAt(cutOffLength: Int, indicationSymbol: String = " ..."): String {
    if (this.length <= cutOffLength || this.length <= indicationSymbol.length) {
        return this
    }
    if (cutOffLength <= indicationSymbol.length) {
        return this.substring(0, cutOffLength)
    }
    return this.substring(0, cutOffLength - indicationSymbol.length) + indicationSymbol
}
