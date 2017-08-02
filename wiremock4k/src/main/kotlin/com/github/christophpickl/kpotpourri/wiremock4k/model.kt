package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder

/**
 * Wiremock always is reachable via localhost.
 */
val WIREMOCK4K_HOSTNAME = "localhost"

/**
 * The default port to be used if nothing was explicitly set.
 *
 * Could use wiremock's dynamicPort() instead.
 */
val DEFAULT_WIREMOCK4K_PORT = 9987

/**
 * Enumeration of all available Wiremock HTTP methods.
 *
 * Adding possibility to use methods as an object, indirecting to available methods via function calls only.
 */
@Suppress("KDocMissingDocumentation")
enum class WiremockMethod(val requestMethod: RequestMethod) {
    GET(RequestMethod.GET) {
        override fun stubForPath(path: String) = get(urlEqualTo(path))!!
        override fun requestedFor(path: String) = getRequestedFor(urlEqualTo(path))!!
    },
    POST(RequestMethod.POST) {
        override fun stubForPath(path: String) = post(urlEqualTo(path))!!
        override fun requestedFor(path: String) = postRequestedFor(urlEqualTo(path))!!
    },
    PUT(RequestMethod.PUT) {
        override fun stubForPath(path: String) = put(urlEqualTo(path))!!
        override fun requestedFor(path: String) = putRequestedFor(urlEqualTo(path))!!
    },
    DELETE(RequestMethod.DELETE) {
        override fun stubForPath(path: String) = delete(urlEqualTo(path))!!
        override fun requestedFor(path: String) = deleteRequestedFor(urlEqualTo(path))!!
    },
    PATCH(RequestMethod.PATCH) {
        override fun stubForPath(path: String) = patch(urlEqualTo(path))!!
        override fun requestedFor(path: String) = patchRequestedFor(urlEqualTo(path))!!
    }
    ;

    /** For preparing. */
    abstract fun stubForPath(path: String): MappingBuilder

    /** For verification. */
    abstract fun requestedFor(path: String): RequestPatternBuilder

}
