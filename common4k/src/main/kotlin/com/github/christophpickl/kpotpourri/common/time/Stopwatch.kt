package com.github.christophpickl.kpotpourri.common.time

import com.github.christophpickl.kpotpourri.common.logging.LOG

/**
 * Enhanced Timing as measureTimeMillis isn't good enough ;)
 */
object Stopwatch {

    private val log = LOG {}

    /**
     * Prints a log message on level INFO containing the elapsed seconds.
     */
    fun <T> elapse(logPrefix: String, callback: () -> T): T {
        val start = System.currentTimeMillis()
        val result = callback()
        val seconds = (System.currentTimeMillis() - start) / 1000
        log.info { "$logPrefix took $seconds seconds." }
        return result
    }

}
