package com.github.christophpickl.kpotpourri.http4k

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
