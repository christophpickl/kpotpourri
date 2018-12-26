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
