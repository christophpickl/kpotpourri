package com.github.christophpickl.kpotpourri.wiremock4k.response

import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.HttpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import mu.KotlinLogging.logger

private val log = logger {}

/**
 * @param path relative URL, like "/my"
 */
fun givenWiremock(
        method: WiremockMethod = WiremockMethod.GET,
        path: String = "/",
        // to access SC_200_Ok instead, it needs to be outsourced to sub-module first (to avoid cyclic deps)
        statusCode: Int = 200,
        responseBody: String? = null,
        withResponse: ResponseDefinitionBuilder.() -> Unit = {}) {
    log.debug { "given wirmock for  ${method.name} $path (status=$statusCode, body=...)" }
    WireMock.stubFor(method.stubForPath(path).willReturn(
            WireMock.aResponse()
                    .withStatus(statusCode)
                    .withBody(responseBody)
                    .apply { withResponse(this) }))
}


/**
 * Use kotlin features to simplify building header instances.
 */
fun ResponseDefinitionBuilder.withHeaders(vararg headers: Pair<String, String>) = apply {
    withHeaders(HttpHeaders(headers.map { HttpHeader(it.first, it.second) }))
}
