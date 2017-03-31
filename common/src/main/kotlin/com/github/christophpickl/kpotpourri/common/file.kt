package com.github.christophpickl.kpotpourri.common

import java.io.File
import java.nio.file.Files

fun File.ensureExists() {
    if (!exists()) {
        throw RuntimeException("Expected a file existing at: $absolutePath")
    }
}

fun File.move(target: File) {
    Files.move(this.toPath(), target.toPath())
}
