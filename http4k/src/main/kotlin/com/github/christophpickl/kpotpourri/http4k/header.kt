package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.internal.HeadersMap

/**
 * Reusable interface to configure HTTP headers.
 */
interface HeadersConfigurable {

    /**
     * Actual container data class storing all headers.
     */
    val headers: HeadersMap

    /**
     * Shortcut to add the `Accept` header.
     */
    fun acceptHeader(value: String) {
        addHeader("Accept" to value)
    }

    /**
     * Add (or override) given header.
     */
    fun addHeader(header: Pair<String, String>) {
        headers += header
    }

}
