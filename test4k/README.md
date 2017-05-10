# Test4k

Test extensions using TestNG, hamkrest and mockito-kotlin.

```kotlin
import com.github.christophpickl.kpotpourri.test4k.skip
import org.testng.annotations.Test

// instead of throwing a SkipException which leads to a warning about unreachable code ...
@Test fun `test is still work in progress`() {
    skip("WIP")
    // ... no warning about unreachable code
} 

```
