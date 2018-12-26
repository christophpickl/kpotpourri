package com.github.christophpickl.kpotpourri.swing

import mu.KotlinLogging.logger
import javax.swing.JOptionPane

class AbortingExceptionHandler : Thread.UncaughtExceptionHandler {
    
    private val log = logger {}

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        log.error(throwable) { "Unhandled exception in: $thread" }
        val message = "${throwable.javaClass.simpleName}: ${throwable.message}"
        JOptionPane.showOptionDialog(
            null,
            message, "Unexpected Error",
            JOptionPane.OK_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            arrayOf("Quit"),
            "Quit"
        )
        System.exit(1)
    }

}
