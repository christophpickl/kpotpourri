package com.github.christophpickl.kpotpourri.markdown4k.internal

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class MarkdownParserTest {

    fun `extractKotlinCode - Given MD with some Kotlin and Xml, Should extract Kotlin code`() {
        val result = MarkdownParser.extractKotlinCode("""
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
        assertThat(result, equalTo(listOf(CodeAndLineNumber(6, "this is kotlin\n"))))
    }

}
