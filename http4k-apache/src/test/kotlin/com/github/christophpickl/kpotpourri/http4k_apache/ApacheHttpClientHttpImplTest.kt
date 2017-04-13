package com.github.christophpickl.kpotpourri.http4k_apache

import com.github.christophpickl.kpotpourri.common.string.combineUrlParts
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.test4k.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock.*

class ApacheHttpClientHttpImplTest : WiremockTest() {

    private val testee get() = ApacheHttpClientHttpImpl(com.github.christophpickl.kpotpourri.http4k.internal.MetaMap())
    private val mockUrl = "/foo"
    private val mockResponseBody = "bar"
    private val mockResponseStatus = 200

    fun `When execute GET, Then return Response4k with OK 200 and string response body`() {
        stubFor(get(urlEqualTo(mockUrl))
                .willReturn(aResponse()
                        .withStatus(mockResponseStatus)
                        .withBody(mockResponseBody)))

        val response = testee.execute(Request4k(
                method = HttpMethod4k.GET,
                url = combineUrlParts(wiremockBaseUrl, mockUrl)
        ))

        response.statusCode shouldMatchValue mockResponseStatus
        response.bodyAsString shouldMatchValue mockResponseBody
        verifyWiremockGet(MockRequest(mockUrl))
    }

}