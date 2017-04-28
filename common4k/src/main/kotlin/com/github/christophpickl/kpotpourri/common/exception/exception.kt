package com.github.christophpickl.kpotpourri.common.exception

import java.io.PrintWriter
import java.io.StringWriter

/**
 * Invokes the given lambda, catches any Exception and init it as the cause of `this`.
 *
 * Usage:
 *
 * RuntimeException("my message").tryOrRethrow {
 *   somethingWhichThrows()
 * }
 */
fun <E: RuntimeException> E.tryOrRethrow(action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        throw this.initCause(e)
    }
}

/**
 * Get the common stack trace representation as an ordinary string.
 */
fun Throwable.stackTraceAsString(): String {
    val stringWriter = StringWriter()
    val printWriter = PrintWriter(stringWriter)
    printStackTrace(printWriter)
    printWriter.flush()
    return stringWriter.toString()
}

/**
 * Nicely format a list of StackTraceElements.
 *
 * Usage:
 *
 * val formattedStackTrace = Exception().stackTrace.formatted()
 */
fun Array<StackTraceElement>.formatted() =
        map { "${it.className}#${it.methodName}() at ${it.fileName}:${it.lineNumber}" }
