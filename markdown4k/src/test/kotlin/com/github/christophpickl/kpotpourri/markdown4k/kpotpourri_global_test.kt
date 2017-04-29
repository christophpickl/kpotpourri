package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get
import org.testng.annotations.Test
import java.io.File

@Test class KPotpourriMarkdownKotlinCheckerTest {

    fun `check all kpotpourri MD files for valid Kotlin code`() {
        // TODO would be nicer to create test cases at runtime for each snippet
        checkMarkdownFilesContainValidKotlinCode(File("../"), ignoreFolders = listOf("src", "build"))
    }

}
