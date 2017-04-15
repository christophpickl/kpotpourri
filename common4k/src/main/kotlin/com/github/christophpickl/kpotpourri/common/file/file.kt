package com.github.christophpickl.kpotpourri.common.file

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.logging.LOG
import java.io.File
import java.nio.file.Files

private val log = LOG {}

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

fun File.resetDirectory(): File {
    log.trace { "Reset directory: $canonicalPath" }
    if (!deleteRecursively()) {
        throw KPotpourriException("Could not delete directory: $canonicalPath")
    }
    mkdirsIfNecessary()
    return this
}

fun File.mkdirsIfNecessary(): File {
    if (!exists()) {
        if (mkdirs()) {
            log.debug { "Created directory: $canonicalPath" }
        } else {
            throw KPotpourriException("Failed to created directory at: $canonicalPath")
        }
    } else if (!isDirectory) {
        throw KPotpourriException("Directory must be a directory at: $canonicalPath")
    } else {
        log.debug { "Directory already exists at: $canonicalPath" }
    }
    return this
}
