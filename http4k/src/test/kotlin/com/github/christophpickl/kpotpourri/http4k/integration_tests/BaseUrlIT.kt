package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.HttpProtocol
import com.github.christophpickl.kpotpourri.http4k.UrlConfig
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_HOSTNAME
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_PORT


abstract class BaseUrlIT(restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    fun `Given Http4k without baseUrl, When request, Then URL was called`() {
        givenGetMockEndpointUrl()

        buildHttp4k {
            baseUrlDisabled()
        }
                .get<Any>(mockWiremockUrlAndEndpointUrl)

        verifyWiremockGet(MockRequest(mockEndpointUrl))
    }

    fun `Given Http4k with baseUrl as string, When request, Then URL was called`() {
        givenGetMockEndpointUrl()

        buildHttp4k {
            baseUrlBy(wiremockBaseUrl)
        }.get<Any>(mockEndpointUrl)

        verifyWiremockGet(MockRequest(mockEndpointUrl))
    }

    fun `Given Http4k with baseUrl as config, When request, Then URL was called`() {
        givenGetMockEndpointUrl()

        buildHttp4k {
            baseUrlBy(UrlConfig(
                    protocol = HttpProtocol.Http,
                    hostName = WIREMOCK_HOSTNAME,
                    port = WIREMOCK_PORT
            ))
        }
                .get<Any>(mockEndpointUrl)

        verifyWiremockGet(MockRequest(mockEndpointUrl))
    }

}
