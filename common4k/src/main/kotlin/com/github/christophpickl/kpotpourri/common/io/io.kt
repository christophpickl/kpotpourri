package com.github.christophpickl.kpotpourri.common.io

import java.io.Closeable


/**
 * Ignores the thrown exception when invoking `Closeable.close()`.
 */
fun Closeable.closeSilently() {
    try {
        close()
    } catch (ignored: Exception) {
        // could introduce slf4j backed module, which provides a different closeQuietly() ;)
        // LOG_Closeable.warn("Could not close '${this}'!", e)
    }
}
