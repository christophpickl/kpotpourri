package com.github.christophpickl.kpotpourri.common.file

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import mu.KotlinLogging.logger
import java.io.File
import java.nio.file.Files

private val log = logger {}

/**
 * Data structure containing the filename and the suffix (without leading dot).
 */
data class FileName(
        val name: String,
        val suffix: String?
) {
    companion object {
        /**
         * Factory method.
         */
        fun by(fullName: String): FileName {
            if (!fullName.contains(".")) {
                return withoutSuffix(fullName)
            }
            if (fullName.startsWith(".")) {
                val secondLastDot = fullName.removePrefix(".").lastIndexOf(".")
                if (secondLastDot == -1) {
                    return withoutSuffix(fullName)
                }
                return extract(fullName, secondLastDot + 1)
            }
            if (fullName.endsWith(".")) {
                val secondLastDot = fullName.removeSuffix(".").lastIndexOf(".")
                if (secondLastDot == -1) {
                    return withoutSuffix(fullName)
                }
                return FileName(fullName.substring(0, secondLastDot + 1), fullName.substring(secondLastDot + 1))
            }
            return extract(fullName, fullName.lastIndexOf("."))
        }

        private fun extract(fullName: String, lastDot: Int) =
                if (lastDot == fullName.length - 1) {
                    withoutSuffix(fullName)
                } else {
                    FileName(fullName.substring(0, lastDot), fullName.substring(lastDot + 1))
                }

        private fun withoutSuffix(fullName: String) = FileName(fullName, null)

    }
}

/**
 * Create a temporary file which deletes itself when the JVM exits.
 */
fun tempFile(name: String): File {
    val fileName = FileName.by(name)
    val temp = File.createTempFile(fileName.name, fileName.suffix?.let { ".$it" })
    temp.deleteOnExit()
    return temp
}

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
        throw KPotpourriException("Expected to be a file: $canonicalPath")
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
fun File.move(target: File) {
    log.debug { "move() ... from ${this.absolutePath} to ${target.absolutePath}" }
    Files.move(this.toPath(), target.toPath())
}

/**
 * Create file if not existing and all of its directory structure.
 */
fun File.touch() {
    parentFile.mkdirs()
    createNewFile()
}


/**
 * Delete the directory recursively and recreate the directory structure.
 */
fun File.resetDirectory(): File {
    log.debug { "Delete and recreate directory: $canonicalPath" }
    if (exists() && !isDirectory) {
        throw KPotpourriException("Expected to be a directory: $canonicalPath")
    }
    if (exists() && !deleteRecursively()) {
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


/**
 * Format given byte count into a proper readable format like "256 KB" or "12 MB".
 */
fun toHumanReadable(bytes: Long): String {
    val kilo = bytes / 1024
    if (kilo < 1024) {
        return "$kilo KB"
    }
    val mega = kilo / 1024
    if (mega < 1024) {
        return "$mega MB"
    }
    val giga = mega / 1024
    return "$giga GB"
}

/**
 * See toHumanReadable(bytes: Long)
 */
fun toHumanReadable(bytes: Int) = toHumanReadable(bytes.toLong())

/**
 * See toHumanReadable(bytes: Long)
 */
val File.humanReadableSize: String get() = toHumanReadable(length())

/**
 * See toHumanReadable(bytes: Long)
 */
val ByteArray.humanReadableSize: String get() = toHumanReadable(size)
