package com.github.christophpickl.kpotpourri.http4k.module_tests

import com.github.christophpickl.kpotpourri.http4k.get
import org.testng.annotations.Test

@Test class QueryParamsTest : ComponentTest() {

    fun `Given same query param in url, globals and request, Then request param should have precedence`() {
        wheneverExecuteHttpMockReturnResponse()
        val http4k = http4kWithMock(withGlobals = {
            baseUrlBy(testUrl)
            addQueryParam("key" to "global")
        })

        http4k.get<Any>("testEndpoint?key=inUrl") {
            addQueryParam("key" to "request")
        }

        verifyHttpMockExecutedWithRequest(url = "$testUrl/testEndpoint?key=request")
    }

    fun `Given same query param in url and globals, Then globals param should have precedence`() {
        wheneverExecuteHttpMockReturnResponse()
        val http4k = http4kWithMock(withGlobals = {
            baseUrlBy(testUrl)
            addQueryParam("key" to "global")
        })

        http4k.get<Any>("testEndpoint?key=inUrl")

        verifyHttpMockExecutedWithRequest(url = "$testUrl/testEndpoint?key=global")
    }

    fun `Given same query param in url and request, Then request param should have precedence`() {
        wheneverExecuteHttpMockReturnResponse()
        val http4k = http4kWithMock(withGlobals = {
            baseUrlBy(testUrl)
        })

        http4k.get<Any>("testEndpoint?key=inUrl") {
            addQueryParam("key" to "request")
        }

        verifyHttpMockExecutedWithRequest(url = "$testUrl/testEndpoint?key=request")
    }
}
