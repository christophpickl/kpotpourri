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
