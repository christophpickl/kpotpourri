package com.github.christophpickl.kpotpourri.common.file

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import mu.KotlinLogging
import java.io.File
import java.nio.file.Files

private val log = KotlinLogging.logger {}

/**
 * Throws an exception if that file does not exist.
 */
fun File.verifyExists(): File { // formerly ensureExists()
    if (!exists()) {
        throw KPotpourriException("Expected a file existing at: $absolutePath")
    }
    return this
}

/**
 * Move a file by using JDK7's Files class.
 */
fun File.move(target: File) {
    log.debug { "move() ... from ${this.absolutePath} to ${target.absolutePath}" }
    Files.move(this.toPath(), target.toPath())
}
