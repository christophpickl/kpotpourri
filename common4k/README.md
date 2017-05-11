# Common4k

Common extensions on different aspects of the language, basic types, strings, collections, files, control structures,
exceptions, logging, et cetera.

Here are some highlights pointed out but you might want to visit the [KDoc](https://christophpickl.github.io/kdoc/common4k/index.html) for
a more complete overview of the provided functionality.

## Misc

* `fun Long.timify(format: MsTimification): String`
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


## Collection

* `fun <K, V> MutableMap<K, V>.put(pair: Pair<K, V>): V?`
* `fun <K, V> mapsOf(vararg maps: Map<K, V>): Map<K, V>`
* `fun List<Any>.prettyPrint(prefix: String, joiner: String)`
* `fun <K, V> Iterable<Pair<K, V>>.toMutableMap()`

## File

* `fun tempFile(name: String): File`
* `fun File.verifyExists(): File`
* `fun File.move(target: File)`
* `fun File.touch()`

## Logging

The new [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) library is a handy wrapper over good old slf4j, but we can do even better:

```kotlin
import com.github.christophpickl.kpotpourri.common.logging.LOG
import mu.KotlinLogging
import org.slf4j.LoggerFactory

class Logee {
    // first we did this with plain slf4j:
    val slf4jLog = LoggerFactory.getLogger(javaClass)
    
    // then kotlin-logging came:
    val kotlinLog = KotlinLogging.logger { }

    // finally with common4k it became:
    val log = LOG {}
}
```
