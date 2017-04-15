package com.github.christophpickl.kpotpourri.http4k.module_tests

import com.github.christophpickl.kpotpourri.http4k.BasicAuth
import org.testng.annotations.Test


@Test class AuthTest : ComponentTest() {

    private val username = "authUsername"
    private val password = "authPassword"
    private val authHeaderValue = "Basic YXV0aFVzZXJuYW1lOmF1dGhQYXNzd29yZA=="

    fun `auth request`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kGet(withRequest = { basicAuth = BasicAuth(username, password) })

        verifyHttpMockExecutedWithRequest(headers = mapOf("Authorization" to authHeaderValue))
    }

    fun `auth global`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kGet(withGlobals = { basicAuth = BasicAuth(username, password) })

        verifyHttpMockExecutedWithRequest(headers = mapOf("Authorization" to authHeaderValue))
    }

}
