package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.common.logging.LOG
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.PrintStream

private val log = LOG {}

/**
 * Ignores the thrown exception when invoking `Closeable.close()`.
 */
fun Closeable.closeSilently() {
    try {
        close()
    } catch (ignored: Exception) {
        log.warn(ignored, { "Exception was thrown while trying to close: $this" })
    }
}

fun type(enter: String, action: () -> Unit): String =
        readAndWriteStdOutIn(enter, action)

fun hitEnter(action: () -> Unit): String {
    return readAndWriteStdOutIn("\n", action)
}

fun readAndWriteStdOutIn(enter: String, action: () -> Unit): String =
        readFromStdOut {
            writeToStdIn(enter) {
                action()
            }
        }

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

fun writeToStdIn(data: String, actionWhichReadsFromStdIn: () -> Unit) {
    val old = System.`in`
    try {
        System.setIn(ByteArrayInputStream(data.toByteArray()))
        actionWhichReadsFromStdIn()
    } finally {
        System.setIn(old)
    }
}
