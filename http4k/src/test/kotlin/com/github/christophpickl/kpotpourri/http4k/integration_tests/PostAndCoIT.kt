package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.test4k.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.natpryce.hamkrest.assertion.assertThat


abstract class PostAndCoIT(restClient: RestClientProducer) : Http4kWiremockTest(restClient) {

    companion object {
        private val REQUEST_STRING_BODY = "test request body"
    }

    // POST
    // =================================================================================================================

    fun `Given default Http4k, When POST, Then should be received`() {
        givenPostToMockEndpointUrl()

        http4k.post(mockEndpointUrl)

        verifyPostRequest(mockEndpointUrl)
    }

    fun `Given default Http4k, When POST with JSON response, Then response DTO should be returned`() {
        stubFor(WireMock.post(WireMock.urlEqualTo(mockEndpointUrl))
                .willReturn(WireMock.aResponse()
                        .withBody(com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy.toJson())))

        val dto = http4k.post(mockEndpointUrl, PersonDto::class)

        dto shouldMatchValue com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy
    }

    fun `Given default Http4k, When POST with JSON body, Then body should be received and content type set`() {
        givenPostToMockEndpointUrl()

        http4k.post(mockEndpointUrl) {
            requestBody(com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy)
        }

        verifyPostRequest(mockEndpointUrl) {
            withRequestBody(equalTo(com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy.toJson()))
            withHeader("content-type", equalTo("application/json"))
        }
    }

    fun `Given default Http4k, When POST with JSON body and custom content type, Then default content type should have been overridden`() {
        givenPostToMockEndpointUrl()

        http4k.post(mockEndpointUrl) {
            requestBody(com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy)
            headers += "content-type" to "application/foobar"
        }

        verifyPostRequest(mockEndpointUrl) {
            withHeader("content-type", equalTo("application/foobar"))
        }

    }

    fun `Given default Http4k, WHen POST with request body disabled, Then no body should have been sent`() {
        givenPostToMockEndpointUrl()

        http4k.post(mockEndpointUrl) {
            requestBodyDisabled()
        }

        verifyPostRequest(mockEndpointUrl) {
            withRequestBody(equalTo(""))
        }
    }

    fun `Given default Http4k, When POST with string request body, Then string body should have been sent`() {
        givenPostToMockEndpointUrl()

        http4k.post(mockEndpointUrl) {
            requestBody(REQUEST_STRING_BODY)
        }

        verifyPostRequest(mockEndpointUrl) {
            withRequestBody(equalTo(REQUEST_STRING_BODY))
        }
    }

    fun `Given default Http4k, When POST with JSON request body and custom response body, Then both should be ok`() {
        val requestDto = com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy1
        val responseDto  = com.github.christophpickl.kpotpourri.http4k.integration_tests.PersonDto.Companion.dummy2
        givenPostToMockEndpointUrl() {
            withBody(responseDto.toJson())
        }

        val actualResponseDto = http4k.post(mockEndpointUrl, PersonDto::class) {
            requestBody(requestDto)
        }

        verifyPostRequest(mockEndpointUrl) {
            withRequestBody(equalTo(requestDto.toJson()))
        }
        assertThat(actualResponseDto, com.natpryce.hamkrest.equalTo(responseDto))
    }

    private fun givenPostToMockEndpointUrl(
            body: String? = null,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}
    ) {
        givenWiremock(
                method = WiremockMethod.POST,
                path = mockEndpointUrl,
                body = body,
                withResponse = withResponse
        )
    }

    // PUT
    // =================================================================================================================

    // DELETE
    // =================================================================================================================


}