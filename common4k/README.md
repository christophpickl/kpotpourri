# Common4k

Common extensions on different areas of the language, basic types, strings, collections, files, control structures,
exceptions and even logging.

Here are some highlights pointed out, but you might want to visit the [KDoc](https://christophpickl.github.io/kdoc/common4k/index.html).

## Misc

* `fun Long.timify(format: MsTimification): String`
* `val Any.enforceAllBranchesCovered: Unit get() = Unit`
* `fun <IN, OUT> IN?.nullOrWith(wither: (IN) -> OUT): OUT?`
* `fun Throwable.stackTraceAsString(): String`
* `fun Array<StackTraceElement>.formatted(): List<String>`

## Numbers

* `fun Int.isBetweenInclusive(lower: Int, upper: Int): Boolean`
* `fun Int.forEach(code: () -> Unit)`
* `fun Double.format(digits: Int): String`
* `fun Double.toSeconds(digits: Int = 3, suffix: String = " secs"): String`

## String

* `fun String.removePreAndSuffix(search: String)`
* `fun String.containsAll(vararg substrings: String, ignoreCase: Boolean = false)`
* `operator fun StringBuilder.plusAssign(char: Char)`
* `fun String.saveToFile(target: File)`

## Logging

```kotlin
import com.github.christophpickl.kpotpourri.common.logging.LOG
import mu.KotlinLogging
import org.slf4j.LoggerFactory

class Logee {
    // first we did this:
    val slf4jLog = LoggerFactory.getLogger(javaClass)
    
    // then kotlin came:
    val kotlinLog = KotlinLogging.logger { }

    // finally with common4k:
    val log = LOG {}
}
```

## Collection

* `fun <K, V> MutableMap<K, V>.put(pair: Pair<K, V>): V?`
* `fun <K, V> mapsOf(vararg maps: Map<K, V>): Map<K, V>`

```kotlin
import com.github.christophpickl.kpotpourri.common.collection.prettyPrint

listOf("a", "b").prettyPrint()
/*
- a
- b
 */
```

## File

* `fun tempFile(name: String): File`
* `fun File.verifyExists(): File`
* `fun File.move(target: File)`
* `fun File.touch()`
