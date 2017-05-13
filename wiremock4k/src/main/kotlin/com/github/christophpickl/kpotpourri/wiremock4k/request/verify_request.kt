package com.github.christophpickl.kpotpourri.wiremock4k.request

import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder

/**
 * Verify a GET request was made.
 */
fun verifyGetRequest(path: String, withRequest: RequestPatternBuilder.() -> Unit = {}) {
    verifyRequest(WiremockMethod.GET, path, withRequest)
}

/**
 * Verify a POST request was made.
 */
fun verifyPostRequest(path: String, withRequest: RequestPatternBuilder.() -> Unit = {}) {
    verifyRequest(WiremockMethod.POST, path, withRequest)
}

/**
 * Verify a PUT request was made.
 */
fun verifyPutRequest(path: String, withRequest: RequestPatternBuilder.() -> Unit = {}) {
    verifyRequest(WiremockMethod.PUT, path, withRequest)
}

/**
 * Verify a DELETE request was made.
 */
fun verifyDeleteRequest(path: String, withRequest: RequestPatternBuilder.() -> Unit = {}) {
    verifyRequest(WiremockMethod.DELETE, path, withRequest)
}

/**
 * Adds HTTP method to be defined dynamically as enum object.
 */
fun verifyRequest(
        method: WiremockMethod,
        path: String,
        withRequest: RequestPatternBuilder.() -> Unit = {}
) {
    val builder = method.requestedFor(path)
    withRequest(builder)
    WireMock.verify(1, builder)
}


/**
 * Checks if the the given header is present (multiple header values supported).
 */
fun Request.hasHeader(pair: Pair<String, String>) =
        headers.getHeader(pair.first).containsValue(pair.second)

/**
 * Check header value for equality (instead of complex `StringValuePattern`).
 */
fun RequestPatternBuilder.withHeader(key: String, expectedValue: String) = apply {
    withHeader(key, WireMock.equalTo(expectedValue))
}

/**
 * Check request body contained the given bytes.
 */
fun RequestPatternBuilder.withRequestBody(bytes: ByteArray) = apply {
    withRequestBody(WireMock.equalTo(String(bytes)))
}


/**
 * Check cookie for equality by default (instead of complex `StringValuePattern`.
 */
fun RequestPatternBuilder.withCookie(key: String, expectedValue: String) = apply {
    withCookie(key, WireMock.equalTo(expectedValue))
}
