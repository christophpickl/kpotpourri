# Common4k

* Exception stack trace handling
* `fun Long.timify(format: MsTimification): String`
* `val Any.enforceAllBranchesCovered: Unit get() = Unit`
* `fun <IN, OUT> IN?.nullOrWith(wither: (IN) -> OUT): OUT?`

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
// instead of:
val oldLog = KotlinLogging.logger { }

// use a shortcut function
val log = LOG {}
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
