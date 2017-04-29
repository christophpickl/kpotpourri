package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.google.common.base.MoreObjects
import java.io.File
import javax.script.Compilable
import javax.script.ScriptEngineManager
import javax.script.ScriptException

private val log = LOG {}

// FIXME get rid of warning about Kotlin runtime library

// TODO move to common4k
fun File.nameStartingFrom(other: File) =
        this.canonicalPath.substring(other.canonicalPath.length)

data class MarkdownSnippet(
        val relativePath: String,
        val markdown: File,
        val lineNumber: Int,
        val code: String
) {
    override fun toString() = MoreObjects.toStringHelper(this)
            .add("relativePath", relativePath)
            .add("lineNumber", lineNumber)
            .add("code", code.substring(0, Math.min(code.length, 10))) // TODO add in common4k to cut with " ..." at the end
            .toString()
}

fun collectMarkdownSnippets(root: File, ignoreFolders: List<String> = emptyList()): List<MarkdownSnippet> {
    val result = mutableListOf<MarkdownSnippet>()
    scanForMdFiles(root, ignoreFolders).forEach { file ->
        val snippets = extractKotlinCode(file.readText())
        snippets.forEach { snippet ->
            result += MarkdownSnippet(
                    relativePath = file.nameStartingFrom(root),
                    markdown = file,
                    lineNumber = snippet.lineNumber,
                    code = snippet.code
            )
        }
    }
    return result
}

fun List<MarkdownSnippet>.toDataProvider(): Array<Array<out Any>> =
        map { arrayOf(it) }.toTypedArray()

fun checkMarkdownFilesContainValidKotlinCode(root: File, ignoreFolders: List<String> = emptyList()) {
    log.info { "Checking directory for valid kotlin code in MD files: ${root.canonicalPath}" }
    val errors = mutableListOf<String>()
    scanForMdFiles(root, ignoreFolders).forEach { file ->
        log.debug { "Checking MD file: ${file.nameStartingFrom(root)}" }
        val snippets = extractKotlinCode(file.readText())
        snippets.forEach { snippet ->
            val valid = validSnippet(snippet.code)
            if (!valid) {
                errors += "${file.canonicalPath}:${snippet.lineNumber}\n>>>$snippet<<<"
            }
        }
    }
    if (errors.isNotEmpty()) {
        throw KotlinCompileException("Markdown Kotlin compilation failed!\n" + errors.joinToString())
    }
}

/**
 * @throws ScriptException if code is not compileable
 */
fun checkCodeIsCompilable(code: String) {
    if (code.startsWith("/// unsafe")) {
        log.debug { "Skipping Kotlin snippet marked as unsafe" }
        return
    }
    compileKotlin(code)

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
            println("ignoreFolders=$ignoreFolders, file.name=${file.name}, file.path=${file.canonicalPath}")
            if (!ignoreFolders.contains(file.name)) {
                mdFiles.addAll(scanForMdFiles(file, ignoreFolders))
            }
        } else if (file.name.endsWith(".md", ignoreCase = true)) {
            mdFiles += file
        }
    }
    return mdFiles
}

internal data class CodeAndLineNumber(
        val lineNumber: Int,
        val code: String
)

internal fun extractKotlinCode(mdContent: String): List<CodeAndLineNumber> {
    var kotlinFlag = false
    val result = mutableListOf<CodeAndLineNumber>()
    var tempLines = StringBuilder()
    var foundLineNumber = -1
    mdContent.lines().forEachIndexed { lineNumber, line ->
        if (line == "```kotlin") {
            foundLineNumber = lineNumber + 1
            kotlinFlag = true
        } else if (line == "```" && kotlinFlag) {
            kotlinFlag = false
            result += CodeAndLineNumber(foundLineNumber, tempLines.toString())
            tempLines = StringBuilder()
        } else if (kotlinFlag) {
            tempLines.appendln(line)
        }
    }
    return result
}

internal fun compileKotlin(code: String) {
    val engine = ScriptEngineManager().getEngineByExtension("kts") ?: throw Exception("kts not supported by script engine :(")
    log.trace { "Compiling Kotlin code:\n>>>\n$code\n<<<" }
    (engine as Compilable).compile(code)
}

internal class KotlinCompileException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)
