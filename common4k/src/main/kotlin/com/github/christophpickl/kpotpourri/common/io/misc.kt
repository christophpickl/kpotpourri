package com.github.christophpickl.kpotpourri.common.io

import mu.KotlinLogging.logger
import java.io.ByteArrayOutputStream
import java.io.Closeable

private val log = logger {}

/**
 * Ignores (logs) the thrown exception when invoking `Closeable.close()`.
 */
fun Closeable.closeSilently() {
    try {
        close()
    } catch (ignored: Exception) {
        log.warn(ignored) { "Exception was thrown while trying to close: $this" }
    }
}

fun ByteArray.toByteArrayOutputStream(): ByteArrayOutputStream {
    val stream = ByteArrayOutputStream(size)
    stream.write(this, 0, size)
    return stream
}
