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

/**
 * Supported protocols as part of an URL.
 */
enum class HttpProtocol(val urlPrefix: String) {
    /** Unsecure. */
    Http("http"),
    /** Secure. */
    Https("https")
}

/**
 * Abstraction of a full URL split into its separate components.
 */
data class UrlConfig(
        // defaults to: "http://localhost:80"
        val protocol: HttpProtocol = HttpProtocol.Http,
        val hostName: String = "localhost",
        val port: Int = 80,
        val path: String = "" // e.g.: "/rest"
)

/**
 * Supported HTTP methods by Http4k.
 *
 * See for example: com.github.tomakehurst.wiremock.http.RequestMethod
 */
enum class HttpMethod4k(val isRequestBodySupported: Boolean = false) {

    /** Idempontent request. */
    GET(),

    /** Create a new entity. */
    POST(isRequestBodySupported = true),

    /** Update an existing entity, idempotent. */
    PUT(isRequestBodySupported = true),

    /** Delete an existing entity. */
    DELETE(),

    /** Update parts of an existing entity, idempotent. */
    PATCH(isRequestBodySupported = true)

    // OPTIONS(),
    // HEAD(),
    // TRACE(),
    // ANY()
}
