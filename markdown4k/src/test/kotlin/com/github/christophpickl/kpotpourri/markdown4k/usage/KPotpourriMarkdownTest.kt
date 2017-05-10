package com.github.christophpickl.kpotpourri.markdown4k.usage

import com.github.christophpickl.kpotpourri.markdown4k.MarkdownTestngTest
import org.testng.annotations.Test
import java.io.File

/**
 * Checks all MD files in KPotopurri sourcefolder for compiling Kotlin code snippets.
 */
@Test class KPotpourriMarkdownTest : MarkdownTestngTest(
        root = File("../"),
        ignoreFolders = listOf("src", "build", ".git")
)
