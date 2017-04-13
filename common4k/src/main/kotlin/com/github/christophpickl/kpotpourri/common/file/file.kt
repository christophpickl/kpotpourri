package com.github.christophpickl.kpotpourri.common.file

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import mu.KotlinLogging
import java.io.File
import java.nio.file.Files

private val log = KotlinLogging.logger {}

/**
 * Throws an exception if that file does not exist.
 *
 * formerly ensureExists()
 */
fun File.verifyExists() = this.apply {
    if (!exists()) {
        throw KPotpourriException("Expected a file existing at: $absolutePath")
    }
}

fun File.verifyIsFile() = this.apply {
    if (!isFile) {
        throw KPotpourriException("Expected to be a file: $absolutePath")
    }
}

fun File.verifyExistsAndIsFile() = this.apply {
    verifyExists()
    verifyIsFile()
}

/**
 * Move a file by using JDK7's Files class.
 */
fun File.move(target: File) {
    log.debug { "move() ... from ${this.absolutePath} to ${target.absolutePath}" }
    Files.move(this.toPath(), target.toPath())
}
