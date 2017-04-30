package com.github.christophpickl.kpotpourri.common.io

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream


/**
 * Deals with System input/output redirecting.
 */
object Io {

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
     * Simulates pressing ENTER and return output from stdout.
     */
    fun hitEnterAndReadStdout(action: () -> Unit): String {
        return readStdoutAndWriteStdin("\n", action)
    }

}
