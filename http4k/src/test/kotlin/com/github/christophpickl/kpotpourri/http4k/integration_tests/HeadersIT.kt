package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.http4k.internal.HeadersMap
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder


abstract class HeadersIT(private val restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    companion object {
        private val KEY = "headerKey"
        private val KEY1 = "headerKey1"
        private val KEY2 = "headerKey2"
        private val VAL = "headerVal"
        private val VAL1 = "headerVal1"
        private val VAL2 = "headerVal2"
        private val KEY_LOWER = "Key"
        private val KEY_UPPER = "KEY"
    }


    fun `When global is set and request is not set, Then global header is used`() {
        givenGetMockEndpointUrl()

        http4k = buildHttp4kWithGlobalHeaders { headers ->
            headers += KEY to VAL
        }

        http4k.get<Any>(mockEndpointUrl)

        verifyGetMockRequest {
            withHeader(KEY, WireMock.equalTo(VAL))
        }
    }

    fun `When global is not set and request is set, Then request header is used`() {
        givenGetMockEndpointUrl()

        http4k.get<Any>(mockEndpointUrl) {
            headers += KEY to VAL
        }

        verifyGetMockRequest {
            withHeader(KEY, WireMock.equalTo(VAL))
        }
    }

    fun `Given global and request header are set, Then request header is used`() {
        givenGetMockEndpointUrl()

        http4k = buildHttp4kWithGlobalHeaders { headers ->
            headers += KEY1 to VAL
        }

        http4k.get<Any>(mockEndpointUrl) {
            headers += KEY2 to VAL
        }


        verifyGetMockRequest {
            withHeader(KEY2, WireMock.equalTo(VAL))
        }
    }

    fun `When global and request header are set but keys only differ in casing, Then request header is used`() {
        givenGetMockEndpointUrl()

        http4k = buildHttp4kWithGlobalHeaders { headers ->
            headers += KEY_LOWER to VAL1
        }

        http4k.get<Any>(mockEndpointUrl) {
            headers += KEY_UPPER to VAL2
        }

        verifyGetMockRequest {
            withHeader(KEY_UPPER, WireMock.equalTo(VAL2))
            // withoutHeader(KEY_LOWER) NO, checks for key existence but null value
        }
    }

    private fun verifyGetMockRequest(func: RequestPatternBuilder.() -> Unit) {
        val getRequestBuilder = WireMock.getRequestedFor(WireMock.urlEqualTo(mockEndpointUrl))
        func(getRequestBuilder)
        verify(getRequestBuilder)
    }

    private fun buildHttp4kWithGlobalHeaders(func: (HeadersMap) -> Unit) = buildHttp4k {
        overrideHttpClient = restClient()
        baseUrlBy(wiremockBaseUrl)
        func(headers)
    }

}
