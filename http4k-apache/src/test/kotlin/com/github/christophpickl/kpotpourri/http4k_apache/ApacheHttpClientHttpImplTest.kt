package com.github.christophpickl.kpotpourri.http4k_apache

import com.github.christophpickl.kpotpourri.common.string.combineUrlParts
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.internal.MetaMap
import com.github.christophpickl.kpotpourri.http4k.internal.MutableMetaMap
import com.github.christophpickl.kpotpourri.http4k.internal.TimeoutException
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock.*

class ApacheHttpClientHttpImplTest : WiremockTest() {

    private val mockUrl = "/foo"
    private val mockResponseBody = "bar"
    private val mockResponseStatus = 200

    fun `When execute GET, Then return Response4k with OK 200 and string response body`() {
        stubFor(get(urlEqualTo(mockUrl))
                .willReturn(aResponse()
                        .withStatus(mockResponseStatus)
                        .withBody(mockResponseBody)))

        val response = executeOrdinaryGetRequest()

        response.statusCode shouldMatchValue mockResponseStatus
        response.bodyAsString shouldMatchValue mockResponseBody
        verifyWiremockGet(MockRequest(mockUrl))
    }

    fun `Given request timeout set, When response takes longer than timeout, Then throw custom TimeoutException`() {
        val timeout = 500
        stubFor(get(urlEqualTo(mockUrl))
                .willReturn(aResponse()
                        .withFixedDelay(timeout * 2)))

        assertThrown<TimeoutException> {
            executeOrdinaryGetRequest(meta = MutableMetaMap().requestTimeout(timeout))
        }
    }

    private fun executeOrdinaryGetRequest(meta: MetaMap = MetaMap()) = execute(Request4k(
                method = HttpMethod4k.GET,
                url = combineUrlParts(wiremockBaseUrl, mockUrl)
        ), meta)

    private fun execute(request: Request4k, meta: MetaMap = MetaMap()) =
        ApacheHttpClientHttpImpl(meta).execute(request)

}
