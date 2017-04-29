package com.github.christophpickl.kpotpourri.common.file

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.logging.LOG
import java.io.File
import java.nio.file.Files

private val log = LOG {}

/**
 * Checks if the File exists or throws a [KPotpourriException].
 *
 * Formerly known as `ensureExists()`.
 */
fun File.verifyExists() = this.apply {
    if (!exists()) {
        throw KPotpourriException("Expected a file existing at: $absolutePath")
    }
}

/**
 * Checks if it's actually a file and not a directory or throws a [KPotpourriException].
 */
fun File.verifyIsFile() = this.apply {
    if (!isFile) {
        throw KPotpourriException("Expected to be a file: $absolutePath")
    }
}

/**
 * Checks if it's an existing file and not a directory or throws a [KPotpourriException].
 */
fun File.verifyExistsAndIsFile() = this.apply {
    verifyExists()
    verifyIsFile()
}

/**
 * Cuts off the starting part, e.g.: "/top/sub/file.txt".nameStartingFrom("/top") => "/sub/file.txt"
 */
fun File.nameStartingFrom(other: File) =
        this.canonicalPath.substring(other.canonicalPath.length)

/**
 * Move a file by using JDK7's [Files] class.
 */
// MINOR test me
fun File.move(target: File) {
    log.debug { "move() ... from ${this.absolutePath} to ${target.absolutePath}" }
    Files.move(this.toPath(), target.toPath())
}

/**
 * Delete the directory recursively and recreate the directory structure.
 */
fun File.resetDirectory(): File {
    log.debug { "Delete and recreate directory: $canonicalPath" }
    if (!isDirectory) {
        throw KPotpourriException("Expected to be a directory: $canonicalPath")
    }
    if (!deleteRecursively()) {
        throw KPotpourriException("Could not delete directory: $canonicalPath")
    }
    mkdirsIfNecessary()
    return this
}

/**
 * Creates all directories if not yet existing.
 */
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

/**
 * List all contained files and returns only if suffix matches, ignoring folders by name optionally.
 */
fun File.scanForFilesRecursively(
        suffix: String,
        ignoreFolders: List<String> = emptyList()
): List<File> {
    if (!exists() || !isDirectory) {
        throw KPotpourriException("Invalid folder: $canonicalPath")
    }
    val foundFiles = mutableListOf<File>()
    listFiles { _ -> true }.forEach { file ->
        if (file.isDirectory && !ignoreFolders.contains(file.name)) {
            foundFiles.addAll(file.scanForFilesRecursively(suffix, ignoreFolders))
        } else if (file.isFile && file.name.endsWith(".$suffix", ignoreCase = true)) {
            log.trace { "Found proper file while scanning: ${file.canonicalPath}" }
            foundFiles += file
        }
    }
    return foundFiles
}
