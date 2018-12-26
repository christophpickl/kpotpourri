package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.collection.KeyIgnoringCaseMap
import com.github.christophpickl.kpotpourri.common.collection.MapAddable

/**
 * Abstraction of a regular [Map] supporting specifics for a HTTP header (e.g. case-insensitive keys).
 */
class HeadersMap(
        private val _map: KeyIgnoringCaseMap<String> = KeyIgnoringCaseMap<String>(
                disableLoggingOfKeys = listOf("authorization")
        )
) : MapAddable<String, String> by _map {

    /** Increase readability for tests, but read-only only. */
    internal val map: Map<String, String> = _map.toImmutableMap()

}
