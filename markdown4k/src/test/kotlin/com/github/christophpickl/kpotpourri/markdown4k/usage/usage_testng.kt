package com.github.christophpickl.kpotpourri.markdown4k.usage

import com.github.christophpickl.kpotpourri.markdown4k.MarkdownTestngTest
import org.testng.annotations.Test
import java.io.File

/**
 * Example of using Markdown4k together with TestNG.
 */
@Test
class UsageTestngTest : MarkdownTestngTest(
        root = File("."), // assume the current working directory contains the MD files
        ignoreFolders = listOf("src", "build", ".git", "out")
)
