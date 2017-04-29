package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsExactlyInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test
import java.io.File
import javax.script.ScriptException

@Test class Markdown4kTest {

    fun `scanForMdFiles - dir1`() {
        assertThat(scanForMdFiles(File("src/test/resources/dir1")).map { it.name },
                containsExactlyInAnyOrder("md1.md", "sub1.md"))
    }

    fun `extractKotlinCode - Given MD with some Kotlin and Xml, Should extract Kotlin code`() {
        val result = extractKotlinCode("""
# some header

some description.

```kotlin
this is kotlin
```

some xml
```xml
this is not kotlin
```
""")
        assertThat(result, equalTo(listOf(CodeAndLineNumber(5, "this is kotlin\n"))))
    }

    fun `compileKotlin - Given valid code, Should compile`() {
        compileKotlin("""val x = 42""")
    }

    fun `compileKotlin - Given invalid code, Should throw`() {
        assertThrown<ScriptException> {
            compileKotlin("""val x = y""")
        }
    }

    fun `compileKotlin - Given code depending on external lib, Should compile`() {
        compileKotlin("""
import org.testng.annotations.Test

val x = 42
val y = 1
println(x + y)

@Test class FooTest {}
""")
    }

    // TODO test for ignoreFolders
    fun `checkMarkdownFilesContainValidKotlinCode - Given valid code, Should not throw`() {
        checkMarkdownFilesContainValidKotlinCode(File("src/test/resources/correct_kotlin"))
    }

    fun `checkMarkdownFilesContainValidKotlinCode - Given invalid code but resides in ignored folder, Should not throw`() {
        checkMarkdownFilesContainValidKotlinCode(File("src/test/resources/ignoring_kotlin"),
                ignoreFolders = listOf("toBeIgnored"))
    }

    fun `checkMarkdownFilesContainValidKotlinCode - Given unsafe instruction, Should not throw`() {
        checkMarkdownFilesContainValidKotlinCode(File("src/test/resources/unsafe_kotlin"))
    }

    fun `checkMarkdownFilesContainValidKotlinCode - Given invalid code, Should throw`() {
        assertThrown<KotlinCompileException> {
            checkMarkdownFilesContainValidKotlinCode(File("src/test/resources/failing_kotlin"))
        }
    }

}
