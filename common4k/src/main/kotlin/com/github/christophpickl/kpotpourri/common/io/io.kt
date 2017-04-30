package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.common.logging.LOG
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.PrintStream

private val log = LOG {}

/**
 * Ignores (logs) the thrown exception when invoking `Closeable.close()`.
 */
fun Closeable.closeSilently() {
    try {
        close()
    } catch (ignored: Exception) {
        log.warn(ignored, { "Exception was thrown while trying to close: $this" })
    }
}

/**
 * Simulates pressing ENTER and return output from stdout.
 */
fun hitEnterAndReadStdout(action: () -> Unit): String {
    return readStdoutAndWriteStdin("\n", action)
}

/**
 * Write given text to stdin, invoke action and return output from stdout.
 */
fun readStdoutAndWriteStdin(text: String, action: () -> Unit): String =
        readFromStdOut {
            writeToStdIn(text) {
                action()
            }
        }

/**
 * Get printed output written to System.out as String.
 */
fun readFromStdOut(actionWhichWritesToStdOut: () -> Unit): String {
    val old = System.out
    val byteStream = ByteArrayOutputStream()
    val stream = PrintStream(byteStream)
    try {
        System.setOut(stream)
        actionWhichWritesToStdOut()
        System.out.flush()
    } finally {
        System.setOut(old)
    }
    val printed = byteStream.toString()
    stream.close()
    return printed
}

/**
 * Writes given text to System.in and invokes the action so it can read that text.
 */
fun writeToStdIn(text: String, actionWhichReadsFromStdIn: () -> Unit) {
    val old = System.`in`
    try {
        System.setIn(ByteArrayInputStream(text.toByteArray()))
        actionWhichReadsFromStdIn()
    } finally {
        System.setIn(old)
    }
}
