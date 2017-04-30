package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.common.logging.LOG
import java.io.Closeable


private val log = LOG {}

/**
 * If you are too lazy to wrap the expression with a common println(), just do it afterwards :)
 */
fun Any?.println() {
    println(this)
}

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
