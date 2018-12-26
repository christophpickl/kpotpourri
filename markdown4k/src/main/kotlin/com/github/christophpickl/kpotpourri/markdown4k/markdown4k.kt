package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.common.file.nameStartingFrom
import com.github.christophpickl.kpotpourri.common.file.scanForFilesRecursively
import com.github.christophpickl.kpotpourri.markdown4k.internal.Kompiler
import com.github.christophpickl.kpotpourri.markdown4k.internal.MarkdownParser
import mu.KotlinLogging.logger
import java.io.File


/**
 * Collects and verifies Kotlin code snippets within Markdown files.
 */
object Markdown4k {

    private val log = logger {}

    /**
     * Scan the given directory recursively for MD files containing Kotlin code snippets.
     *
     * @param ignoreFolders specify list of directory names which should be skipped in recursive file scan.
     */
    fun kollect(
            root: File,
            ignoreFolders: List<String> = emptyList(),
            suppressIgnoredSnippets: Boolean = true
    ): List<CodeSnippet> {
        log.info { "collectSnippets(root=${root.canonicalPath}, ignoreFolders=$ignoreFolders)" }
        val result = mutableListOf<CodeSnippet>()
        root.scanForFilesRecursively("md", ignoreFolders).forEach { file ->
            MarkdownParser.extractKotlinCode(file.readText()).forEach { (lineNumber, code) ->
                val snippet = CodeSnippet(
                        markdown = file,
                        relativePath = file.nameStartingFrom(root),
                        lineNumber = lineNumber,
                        code = code
                )
                if (!suppressIgnoredSnippets || !snippet.isIgnored) {
                    result += snippet
                }
            }
        }
        return result
    }

    /**
     * Suppress compilation by prefixing the code with "/// ignore".
     */
    fun kompile(snippet: CodeSnippet): KompilationResult {
        if (snippet.isIgnored) {
            log.debug { "Skipping Kotlin snippet as it is marked as unsafe: $snippet" }
            return KompilationResult.Ignored()
        }
        return Kompiler.kompile(snippet.code).result
    }

}
