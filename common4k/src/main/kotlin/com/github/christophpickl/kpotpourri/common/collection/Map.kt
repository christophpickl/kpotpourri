package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.google.common.annotations.VisibleForTesting

/**
 * Limitted interface for a map, to be used as by delegation.
 */
interface MapAddable<K, V> {

    /**
     * Single add method without a default implementation, all other need not to be implemented.
     */
    fun add(key: K, value: V)

    /**
     * Simplify usage, e.g.: "map += 1 to 2"
     */
    operator fun plusAssign(pair: Pair<K, V>) {
        add(pair.first, pair.second)
    }

    /**
     * Add a pair: map.add("a" to 1)
     */
    fun add(pair: Pair<K, V>) {
        add(pair.first, pair.second)
    }

    /**
     * Add a good old map entry.
     */
    fun add(entry: Map.Entry<K, V>) {
        add(entry.key, entry.value)
    }

    /**
     * Add all headers based on given map.
     */
    fun addAll(add: MapAddable<K, V>) {
        addAll(add.toImmutableMap())
    }

    /**
     * Add all headers based on given map.
     */
    fun addAll(add: Map<K, V>) {
        // dont simply putAll, as this would not have the same result
        add.forEach { add(it) }
    }

    /**
     * Convert to a read only map.
     */
    fun toImmutableMap(): Map<K, V>
}

@Suppress("KDocMissingDocumentation")
/**
 * Any added header key is overwritten by comparing case-insensitive; can come handy to store HTTP headers.
 */
class KeyIgnoringCaseMap<V>(
        /** Disable log output for the given keys, e.g. the Authorization header. */
        private val disableLoggingOfKeys: List<String>? = null
) : MapAddable<String, V> {

    private val log = LOG {}

    @VisibleForTesting internal val _map = LinkedHashMap<String, V>()

    // MINOR fun getIncasesensitive(key: String): V

    override fun toImmutableMap(): Map<String, V> = _map

    override fun add(key: String, value: V) {
        removeKeyIfExists(key, value)
        _map += key to value
    }

    private fun removeKeyIfExists(key: String, value: V) {
        _map.keys.firstOrNull { it.toLowerCase() == key.toLowerCase() }?.let {
            if (disableLoggingOfKeys?.contains(it) ?: false) {
                log.debug { "Overriding header '$it' (value is set to be hidden).'" }
            } else {
                log.debug { "Overriding header '$it'. Old value: '${_map[it]}' with new value: '$value'" }
            }
            _map -= it
        }
    }

}


/**
 * Checks if there are the same keys for both maps.
 */
fun <K, V> Map<K, V>.hasIntersection(that: Map<K, V>): Boolean {
    return this.keys.firstOrNull(that::containsKey) != null
}

/**
 * Throws a `KPotpourriException` if hasIntersection() is true
 */
fun <K, V> Map<K, V>.verifyNoIntersection(that: Map<K, V>) {
    if (hasIntersection(that)) {
        throw KPotpourriException("Expected no intersections! This: $this. That: $that.")
    }
}

/**
 * Same as original put() but with Kotlin's Pair instance to allow the "to" syntax.
 *
 * Sample: put(1, "1") => put(1 to "1")
 */
fun <K, V> MutableMap<K, V>.put(pair: Pair<K, V>) =
        put(pair.first, pair.second)

/**
 * Constructs a map of a combination of all given maps.
 */
fun <K, V> mapsOf(vararg maps: Map<K, V>): Map<K, V> =
        LinkedHashMap<K, V>().also { mapOfMaps ->
            maps.forEach(mapOfMaps::putAll)
        }
