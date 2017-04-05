package com.github.christophpickl.kpotpourri.common.file

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import java.io.File
import java.nio.file.Files


/**
 * Throws an exception if that file does not exist.
 */
fun File.verifyExists() { // formerly ensureExists()
    if (!exists()) {
        throw KPotpourriException("Expected a file existing at: $absolutePath")
    }
}

/**
 * Move a file by using JDK7's Files class.
 */
fun File.move(target: File) {
    Files.move(this.toPath(), target.toPath())
}
