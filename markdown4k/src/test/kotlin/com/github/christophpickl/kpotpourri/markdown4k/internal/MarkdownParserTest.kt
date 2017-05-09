package com.github.christophpickl.kpotpourri.markdown4k.internal

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.hasSizeOf
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import org.testng.annotations.Test

@Test class MarkdownParserTest {

    fun `Given empty text, Should return empty`() {
        val result = MarkdownParser.extractKotlinCode("")
        assertThat(result, isEmpty)
    }

    fun `Given Kotlin snippet only, Should extract Kotlin code`() {
        val result = MarkdownParser.extractKotlinCode("""
```kotlin
single snippet
```
""")
        assertThat(result, equalTo(listOf(CodeAndLineNumber(6, "single snippet\n"))))
    }

    fun `Given XML snippet, Should return empty`() {
        val result = MarkdownParser.extractKotlinCode("""
```xml
this is not kotlin
```
""")
        assertThat(result, isEmpty)
    }

    fun `Given two Kotlin snippets, Should extract two snippets`() {
        val result = MarkdownParser.extractKotlinCode("""
```kotlin
first sample
```

```kotlin
second sample
```
""")
        assertThat(result, hasSizeOf(2))
    }

    fun `Given real world sample, Should extract Kotlin code`() {
        val result = MarkdownParser.extractKotlinCode("""
# some header

some description.

```kotlin
val realWorld = 42
```

some xml
```java
this is not kotlin
```
""")
        assertThat(result, equalTo(listOf(CodeAndLineNumber(6, "val realWorld = 42\n"))))
    }

}
