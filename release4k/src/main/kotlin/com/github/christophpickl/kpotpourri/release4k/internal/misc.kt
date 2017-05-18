package com.github.christophpickl.kpotpourri.release4k.internal

import java.io.File

/**
 * Prints out the message with a consistent prompt prefix.
 */
/*pseude-internal*/ fun kout(message: String) {
    println("[KRELEASE] $message")
}

/**
 * Prints out the message with the path as a prefix.
 */
/*pseude-internal*/ fun koutFile(path: File, message: String) {
    kout("[${path.canonicalPath}] $ $message")
}
