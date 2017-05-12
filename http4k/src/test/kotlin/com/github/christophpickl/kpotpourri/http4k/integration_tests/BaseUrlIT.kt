package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.BaseUrlConfig
import com.github.christophpickl.kpotpourri.http4k.HttpProtocol
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_HOSTNAME
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyGetRequest


abstract class BaseUrlIT(restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    fun `Given Http4k without baseUrl, When request, Then URL was called`() {
        givenGetMockEndpointUrl()

        buildHttp4k {
            baseUrlDisabled()
        }
                .get<Any>(mockWiremockUrlAndEndpointUrl)

        verifyGetRequest(mockEndpointUrl)
    }

    fun `Given Http4k with baseUrl as string, When request, Then URL was called`() {
        givenGetMockEndpointUrl()

        buildHttp4k {
            baseUrlBy(wiremockBaseUrl)
        }.get<Any>(mockEndpointUrl)

        verifyGetRequest(mockEndpointUrl)
    }

    fun `Given Http4k with baseUrl as config, When request, Then URL was called`() {
        givenGetMockEndpointUrl()

        buildHttp4k {
            baseUrlBy(BaseUrlConfig(
                    protocol = HttpProtocol.Http,
                    hostName = WIREMOCK_HOSTNAME,
                    port = DEFAULT_WIREMOCK_PORT
            ))
        }
                .get<Any>(mockEndpointUrl)

        verifyGetRequest(mockEndpointUrl)
    }

}
