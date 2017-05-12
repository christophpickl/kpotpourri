package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder

/**
 * Wiremock always is reachable via localhost.
 */
val WIREMOCK_HOSTNAME = "localhost"

/**
 * The default port to be used if nothing was explicitly set.
 *
 * Could use wiremock's dynamicPort() instead.
 */
val DEFAULT_WIREMOCK_PORT = 9987

/**
 * Enumeration of all available Wiremock HTTP methods.
 *
 * Adding possibility to use methods as an object, indirecting to available methods via function calls only.
 */
@Suppress("KDocMissingDocumentation")
enum class WiremockMethod {
    GET() {
        override fun stubForPath(path: String) = get(urlEqualTo(path))!!
        override fun requestedFor(path: String) = getRequestedFor(urlEqualTo(path))!!
    },
    POST() {
        override fun stubForPath(path: String) = post(urlEqualTo(path))!!
        override fun requestedFor(path: String) = postRequestedFor(urlEqualTo(path))!!
    },
    PUT() {
        override fun stubForPath(path: String) = put(urlEqualTo(path))!!
        override fun requestedFor(path: String) = putRequestedFor(urlEqualTo(path))!!
    },
    DELETE() {
        override fun stubForPath(path: String) = delete(urlEqualTo(path))!!
        override fun requestedFor(path: String) = deleteRequestedFor(urlEqualTo(path))!!
    },
    PATCH() {
        override fun stubForPath(path: String) = patch(urlEqualTo(path))!!
        override fun requestedFor(path: String) = patchRequestedFor(urlEqualTo(path))!!
    }
    ;

    /** For preparing. */
    abstract fun stubForPath(path: String): MappingBuilder

    /** For verification. */
    abstract fun requestedFor(path: String): RequestPatternBuilder

}

/**
 * Definition of a mocked request providing the HTTP method "on the fly".
 */
data class MockRequest(
        val path: String,
        val withRequest: RequestPatternBuilder.() -> Unit = {}
)
