package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.internal.implementations.ApacheHttpClientRestClient
import com.github.christophpickl.kpotpourri.test4k.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_DEFAULT_URL
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock.*
import sun.rmi.rmic.Names.stubFor

class ApacheHttpClientRestClientTest : WiremockTest() {

    private val testee get() = ApacheHttpClientRestClient()
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
                url = WIREMOCK_DEFAULT_URL + mockUrl
        ))

        response.statusCode shouldMatchValue mockResponseStatus
        response.bodyAsString shouldMatchValue mockResponseBody
        verifyGetRequest(mockUrl)
    }

}
