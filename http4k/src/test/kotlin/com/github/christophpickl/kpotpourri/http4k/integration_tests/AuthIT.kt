package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.common.string.concatUrlParts
import com.github.christophpickl.kpotpourri.http4k.BasicAuthMode.BasicAuth
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyGetRequest
import com.github.tomakehurst.wiremock.client.WireMock


abstract class AuthIT(restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    private val username = "authUsername"
    private val password = "authPassword"
    private val authHeaderValue = "Basic YXV0aFVzZXJuYW1lOmF1dGhQYXNzd29yZA=="

    fun `Given default Http4k, When GET with basic auth, Then Authorization header is set`() {
        givenGetMockEndpointUrl()

        http4k.get<Any>(mockEndpointUrl) {
            basicAuth = BasicAuth(
                    username = username,
                    password = password
            )
        }

        verifyGetRequest(mockEndpointUrl) {
            withHeader("Authorization", WireMock.equalTo(authHeaderValue))
        }
    }

    fun `Given basic auth configured Http4k, When GET, Then Authorization header is set`() {
        givenGetMockEndpointUrl()
        val http4k = buildHttp4k {
            basicAuth(username, password)
        }

        http4k.get<Any>(concatUrlParts(wiremockBaseUrl, mockEndpointUrl))

        verifyGetRequest(mockEndpointUrl) {
            withHeader("Authorization", WireMock.equalTo(authHeaderValue))
        }
    }

    fun `Given basic auth configured Http4k, When GET with basic auth, Then Authorization header is set by request auth`() {
        givenGetMockEndpointUrl()
        val http4k = buildHttp4k {
            basicAuth("some other", "some password")
        }

        http4k.get<Any>(concatUrlParts(wiremockBaseUrl, mockEndpointUrl)) {
            basicAuth = BasicAuth(
                    username = username,
                    password = password
            )
        }

        verifyGetRequest(mockEndpointUrl) {
            withHeader("Authorization", WireMock.equalTo(authHeaderValue))
        }
    }

}
