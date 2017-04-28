package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.delete
import com.github.christophpickl.kpotpourri.http4k.patch
import com.github.christophpickl.kpotpourri.http4k.post
import com.github.christophpickl.kpotpourri.http4k.put
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.InternalMockRequest
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.natpryce.hamkrest.assertion.assertThat


abstract class PostAndCoIT(restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    companion object {
        private val REQUEST_STRING_BODY = "test request body"
    }

    // POST
    // =================================================================================================================

    fun `Given default Http4k, When POST, Then should be received`() {
        givenPostToMockEndpointUrl()

        http4k.post<Any>(mockEndpointUrl)

        verifyPostRequest(MockRequest(mockEndpointUrl))
    }

    fun `Given default Http4k, When POST with JSON response, Then response DTO should be returned`() {
        stubFor(WireMock.post(WireMock.urlEqualTo(mockEndpointUrl))
                .willReturn(WireMock.aResponse()
                        .withBody(com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy.toJson())))

        val dto = http4k.post<PersonDto>(mockEndpointUrl)

        dto shouldMatchValue com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy
    }

    fun `Given default Http4k, When POST with JSON body, Then body should be received and content type set`() {
        givenPostToMockEndpointUrl()

        http4k.post<Any>(mockEndpointUrl) {
            requestBody(com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy)
        }

        verifyPostRequest(MockRequest(mockEndpointUrl) {
            withRequestBody(equalTo(com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy.toJson()))
            withHeader("content-type", equalTo("application/json"))
        })
    }

    fun `Given default Http4k, When POST with JSON body and custom content type, Then default content type should have been overridden`() {
        givenPostToMockEndpointUrl()

        http4k.post<Any>(mockEndpointUrl) {
            requestBody(com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy)
            headers += "content-type" to "application/foobar"
        }

        verifyPostRequest(MockRequest(mockEndpointUrl, {
            withHeader("content-type", equalTo("application/foobar"))
        }))

    }

    fun `Given default Http4k, WHen POST with request body disabled, Then no body should have been sent`() {
        givenPostToMockEndpointUrl()

        http4k.post<Any>(mockEndpointUrl) {
            requestBodyDisabled()
        }

        verifyPostRequest(MockRequest(mockEndpointUrl, {
            withRequestBody(equalTo(""))
        }))
    }

    fun `Given default Http4k, When POST with string request body, Then string body should have been sent`() {
        givenPostToMockEndpointUrl()

        http4k.post<Any>(mockEndpointUrl) {
            requestBody(REQUEST_STRING_BODY)
        }

        verifyPostRequest(MockRequest(mockEndpointUrl, {
            withRequestBody(equalTo(REQUEST_STRING_BODY))
        }))
    }

    fun `Given default Http4k, When POST with JSON request body and custom response body, Then both should be ok`() {
        val requestDto = com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy1
        val responseDto  = com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy2
        givenPostToMockEndpointUrl {
            withBody(responseDto.toJson())
        }

        val actualResponseDto = http4k.post<PersonDto>(mockEndpointUrl) {
            requestBody(requestDto)
        }

        verifyPostRequest(MockRequest(mockEndpointUrl, {
            withRequestBody(equalTo(requestDto.toJson()))
        }))
        assertThat(actualResponseDto, com.natpryce.hamkrest.equalTo(responseDto))
    }

    private fun givenPostToMockEndpointUrl(
            body: String? = null,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}
    ) {
        givenToMockEndpointUrl(WiremockMethod.POST, body, withResponse)
    }
    private fun givenToMockEndpointUrl(
            method: WiremockMethod,
            body: String? = null,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}
    ) {
        givenWiremock(
                method = method,
                path = mockEndpointUrl,
                body = body,
                withResponse = withResponse
        )
    }

    // PUT
    // =================================================================================================================

    fun `Given default Http4k, When PUT, Then should be received`() {
        givenToMockEndpointUrl(WiremockMethod.PUT)

        http4k.put<Any>(mockEndpointUrl)

        verifyRequest(InternalMockRequest(path = mockEndpointUrl, method = WiremockMethod.PUT))
    }

    // DELETE
    // =================================================================================================================

    fun `Given default Http4k, When DELETE, Then should be received`() {
        givenToMockEndpointUrl(WiremockMethod.DELETE)

        http4k.delete<Any>(mockEndpointUrl)

        verifyRequest(InternalMockRequest(path = mockEndpointUrl, method = WiremockMethod.DELETE))
    }

    // PATCH
    // =================================================================================================================

    fun `Given default Http4k, When PATCH, Then should be received`() {
        givenToMockEndpointUrl(WiremockMethod.PATCH)

        http4k.patch<Any>(mockEndpointUrl)

        if (javaClass.simpleName == "PostAndCoFuelIT") {
            verifyRequest(InternalMockRequest(path = mockEndpointUrl, method = WiremockMethod.POST,
                    func = { withHeader("X-HTTP-Method-Override", equalTo("PATCH"))}))
        } else {
            verifyRequest(InternalMockRequest(path = mockEndpointUrl, method = WiremockMethod.PATCH))
        }
    }

}
