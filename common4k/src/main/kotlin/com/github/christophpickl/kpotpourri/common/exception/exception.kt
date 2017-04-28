package com.github.christophpickl.kpotpourri.common.exception

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

// TODO print stack trace as string
/*
StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            th.printStackTrace(pw);
            pw.flush();
            and get recursively the CAUSE
 */

// TODO test this
fun Array<StackTraceElement>.formatted() =
        map { "${it.className}#${it.methodName}() at ${it.fileName}:${it.lineNumber}" }
