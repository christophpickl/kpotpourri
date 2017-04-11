package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.common.string.concatUrlParts
import com.github.christophpickl.kpotpourri.http4k.BasicAuth
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_DEFAULT_URL
import com.github.tomakehurst.wiremock.client.WireMock


class AuthIT : Http4kWiremockTest() {

    companion object {
        private val USERNAME = "authUsername"
        private val PASSWORD = "authPassword"
        private val HEADER_VALUE = "Basic YXV0aFVzZXJuYW1lOmF1dGhQYXNzd29yZA=="
    }

    fun `Given default Http4k, When GET with basic auth, Then Authorization header is set`() {
        givenGetMockEndpointUrl()

        http4k.get(mockEndpointUrl) {
            basicAuth = BasicAuth(
                    username = USERNAME,
                    password = PASSWORD
            )
        }

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(mockEndpointUrl))
                .withHeader("Authorization", WireMock.equalTo(HEADER_VALUE)))
    }

    fun `Given basic auth configured Http4k, When GET, Then Authorization header is set`() {
        givenGetMockEndpointUrl()
        val http4k = buildHttp4k {
            basicAuth(USERNAME, PASSWORD)
        }

        http4k.get(concatUrlParts(WIREMOCK_DEFAULT_URL, mockEndpointUrl))

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(mockEndpointUrl))
                .withHeader("Authorization", WireMock.equalTo(HEADER_VALUE)))
    }

    fun `Given basic auth configured Http4k, When GET with basic auth, Then Authorization header is set by request auth`() {
        givenGetMockEndpointUrl()
        val http4k = buildHttp4k {
            basicAuth("some other", "some password")
        }

        http4k.get(concatUrlParts(WIREMOCK_DEFAULT_URL, mockEndpointUrl)) {
            basicAuth = BasicAuth(
                    username = USERNAME,
                    password = PASSWORD
            )
        }

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(mockEndpointUrl))
                .withHeader("Authorization", WireMock.equalTo(HEADER_VALUE)))
    }

}
