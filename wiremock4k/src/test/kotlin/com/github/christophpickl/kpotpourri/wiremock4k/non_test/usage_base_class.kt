package com.github.christophpickl.kpotpourri.wiremock4k.non_test

import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyDeleteRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyGetRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyPostRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyPutRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.withCookie
import com.github.christophpickl.kpotpourri.wiremock4k.request.withHeader
import com.github.christophpickl.kpotpourri.wiremock4k.response.givenWiremock
import com.github.christophpickl.kpotpourri.wiremock4k.response.withHeaders
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTest
import org.testng.annotations.Test

@Test(enabled = false) class UsageTest : WiremockTest() {

    private val path = "/rest"

    fun `prepare response`() {
        givenWiremock(
                method = WiremockMethod.GET,
                path = path,
                statusCode = 200,
                responseBody = "hello wiremock4k"
        ) {
            // acting on `ResponseDefinitionBuilder`
            withHeaders("Accept" to "application/json")
        }
    }

    fun `verify request`() {
        println(wiremockBaseUrl) // http://localhost:9987

        verifyGetRequest(path)
        verifyPostRequest(path)
        verifyPutRequest(path)
        verifyDeleteRequest(path)

        verifyGetRequest(path) {
            // acting on `RequestPatternBuilder`
            withHeader("Accept", "application/json")
            withCookie("JESSIONID", "1234")
        }

        // or select HTTP method dynamically by passing an enum instance
        verifyRequest(WiremockMethod.PATCH, path)
    }

    fun `access to mock server`() {
        println(server.isRunning)

        server.findNearMissesForUnmatchedRequests()
    }

}
