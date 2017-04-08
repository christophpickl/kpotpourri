package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.testinfra.shouldMatchValue
import com.github.christophpickl.kpotpourri.http4k.internal.implementations.ApacheHttpClientRestClient
import com.github.christophpickl.kpotpourri.http4k.non_test.WIREMOCK_DEFAULT_URL
import com.github.christophpickl.kpotpourri.http4k.non_test.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock.*

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
