package com.github.christophpickl.kpotpourri.http4k.module_tests

import org.testng.annotations.Test

@Test class BaseUrlTest : ComponentTest() {

    private val testBaseUrl = "http://localhost:8042"

    fun `baseUrl by string`() {
        wheneverExecuteHttpMockReturnResponse()
        val http4k = buildMockHttp4k(withGlobals = { baseUrlBy(testBaseUrl) })

        http4k.get("testEndpoint")

        verifyHttpMockExecutedWithRequest(url = "$testBaseUrl/testEndpoint")
    }

    fun `Given base URL has trailing slash, When GET slash, Then there should only be one trailing slash`() {
        wheneverExecuteHttpMockReturnResponse()
        val http4k = buildMockHttp4k(withGlobals = { baseUrlBy("$testBaseUrl/") })

        http4k.get(url = "/")

        verifyHttpMockExecutedWithRequest(url = "http://localhost:8042")
    }

}
