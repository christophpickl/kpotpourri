package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.logging.LOG
import java.io.File
import javax.script.Compilable
import javax.script.ScriptEngineManager
import javax.script.ScriptException

private val log = LOG {}

// TODO move to common4k
fun File.nameStartingFrom(other: File) =
        canonicalPath.substring(other.canonicalPath.length)

fun checkMarkdownFilesContainValidKotlinCode(root: File, ignoreFolders: List<String> = emptyList()) {
    log.info { "Checking directory for valid kotlin code in MD files: ${root.canonicalPath}" }
    val errors = mutableListOf<String>()
    scanForMdFiles(root, ignoreFolders).forEach { file ->
        log.debug { "Checking MD file: ${file.nameStartingFrom(root)}" }
        val snippets = extractKotlinCode(file.readText())
        snippets.forEach { snippet ->
            val valid = validSnippet(snippet)
            if (!valid) {
                errors += "${file.canonicalPath}\n>>>$snippet<<<"
            }
        }
    }
    if (errors.isNotEmpty()) {
        throw KotlinCompileException("Markdown Kotlin compilation failed!\n" + errors.joinToString())
    }
}

private fun validSnippet(snippet: String): Boolean {
    if (snippet.startsWith("/// unsafe")) {
        log.debug { "Skipping Kotlin snippet marked as unsafe" }
        return true
    }
    try {
        compileKotlin(snippet)
        return true
    } catch (e: ScriptException) {
        log.warn(e, { "Error thrown while trying to compile Kotlin code" })
        return false
    }
}

internal fun scanForMdFiles(root: File, ignoreFolders: List<String> = emptyList()): List<File> {
    val mdFiles = mutableListOf<File>()
    root.listFiles { _ -> true }.forEach { file ->
        if (file.isDirectory) {
            if (!ignoreFolders.contains(file.name)) {
                mdFiles.addAll(scanForMdFiles(file))
            }
        } else if (file.name.endsWith(".md", ignoreCase = true)) {
            mdFiles += file
        }
    }
    return mdFiles
}

internal fun extractKotlinCode(mdContent: String): List<String> {
    var kotlinFlag = false
    val result = mutableListOf<String>()
    var tempLines = StringBuilder()
    mdContent.lines().forEach { line ->
        if (line == "```kotlin") {
            kotlinFlag = true
        } else if (line == "```" && kotlinFlag) {
            kotlinFlag = false
            result += tempLines.toString()
            tempLines = StringBuilder()
        } else if (kotlinFlag) {
            tempLines.appendln(line)
        }
    }
    return result
}

internal fun compileKotlin(code: String) {
    val engine = ScriptEngineManager().getEngineByExtension("kts") ?: throw Exception("kts not supported by script engine :(")
    log.trace { "Compiling Kotlin code:\n>>>$code<<<" }
    (engine as Compilable).compile(code)
}

internal class KotlinCompileException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)
