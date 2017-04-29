package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.logging.LOG
import java.io.File

private val log = LOG {}

// TODO move stuff in here to common4k

fun File.nameStartingFrom(other: File) =
        this.canonicalPath.substring(other.canonicalPath.length)


fun scanForFilesRecursively(
        root: File,
        suffix: String,
        ignoreFolders: List<String> = emptyList()
): List<File> {
    val foundFiles = mutableListOf<File>()
    root.listFiles { _ -> true }.forEach { file ->
        if (file.isDirectory) {
            if (!ignoreFolders.contains(file.name)) {
                foundFiles.addAll(scanForFilesRecursively(file, suffix, ignoreFolders))
            }
        } else if (file.name.endsWith(".$suffix", ignoreCase = true)) {
            log.trace { "Found proper file while scanning: ${file.canonicalPath}" }
            foundFiles += file
        }
    }
    return foundFiles
}
