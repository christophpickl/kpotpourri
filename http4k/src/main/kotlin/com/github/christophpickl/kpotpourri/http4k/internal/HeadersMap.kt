package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG

/**
 * Abstraction of a regular [Map] supporting specifics for a HTTP header (e.g. case-insensitive keys).
 */
class HeadersMap {

    private val log = LOG {}

    private val _map = HashMap<String, String>()

    internal val map: Map<String, String> = _map

    /**
     * Simplify usage like "map += 1 to 2".
     */
    operator fun plusAssign(pair: Pair<String, String>) {
        addEntry(pair.first, pair.second)
    }

    /**
     * Add all entries from another [HeadersMap].
     */
    fun addAll(add: HeadersMap) {
        addAll(add._map)
    }

    /**
     *
     */
    fun addAll(add: Map<String, String>) {
        add.forEach(this::addEntry)
    }

    private fun addEntry(key: String, value: String) {
        map.keys.firstOrNull { it.toLowerCase() == key.toLowerCase() }?.let {
            log.debug { "Overriding header '$it'. Old value: '${hideAuthorization(it, map[it]!!)}' with new value: '${hideAuthorization(it, value)}'" }
            _map -= it
        }
        // ATTENTION: dont print header value as it could contain Authorization secret
        _map += key to value
    }

    private fun hideAuthorization(key: String, value: String) = if (key.equals("authorization", ignoreCase = true)) "xxxxx" else value

}
