package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.common.string.concatUrlParts
import com.github.christophpickl.kpotpourri.http4k.BasicAuth
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest
import com.github.tomakehurst.wiremock.client.WireMock


abstract class AuthIT(restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    private val username = "authUsername"
    private val password = "authPassword"
    private val authHeaderValue = "Basic YXV0aFVzZXJuYW1lOmF1dGhQYXNzd29yZA=="

    fun `Given default Http4k, When GET with basic auth, Then Authorization header is set`() {
        givenGetMockEndpointUrl()

        http4k.get(mockEndpointUrl) {
            basicAuth = BasicAuth(
                    username = username,
                    password = password
            )
        }

        verifyWiremockGet(MockRequest(mockEndpointUrl, {
            withHeader("Authorization", WireMock.equalTo(authHeaderValue))
        }))
    }

    fun `Given basic auth configured Http4k, When GET, Then Authorization header is set`() {
        givenGetMockEndpointUrl()
        val http4k = buildHttp4k {
            basicAuth(username, password)
        }

        http4k.get(concatUrlParts(wiremockBaseUrl, mockEndpointUrl))

        verifyWiremockGet(MockRequest(mockEndpointUrl, {
            withHeader("Authorization", WireMock.equalTo(authHeaderValue))
        }))
    }

    fun `Given basic auth configured Http4k, When GET with basic auth, Then Authorization header is set by request auth`() {
        givenGetMockEndpointUrl()
        val http4k = buildHttp4k {
            basicAuth("some other", "some password")
        }

        http4k.get(concatUrlParts(wiremockBaseUrl, mockEndpointUrl)) {
            basicAuth = BasicAuth(
                    username = username,
                    password = password
            )
        }

        verifyWiremockGet(MockRequest(mockEndpointUrl, {
            withHeader("Authorization", WireMock.equalTo(authHeaderValue))
        }))
    }

}
