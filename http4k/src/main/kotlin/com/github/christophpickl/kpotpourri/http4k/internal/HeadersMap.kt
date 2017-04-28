package com.github.christophpickl.kpotpourri.http4k.internal


class HeadersMap {

    private val _map = HashMap<String, String>()

    val map: Map<String, String> = _map

    operator fun plusAssign(pair: Pair<String, String>) {
        addEntry(pair.first, pair.second)
    }

    fun addAll(add: HeadersMap) {
        addAll(add._map)
    }

    fun addAll(add: Map<String, String>) {
        add.forEach(this::addEntry)
    }

    private fun addEntry(key: String, value: String) {
        map.keys.firstOrNull { it.toLowerCase() == key.toLowerCase() }?.let {
            _map -= it
        }
        // dont print header value as it could contain Authorization secret
        _map += key to value
    }

}
