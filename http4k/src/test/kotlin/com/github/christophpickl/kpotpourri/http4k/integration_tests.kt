package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.testinfra.mapContains
import com.github.christophpickl.kpotpourri.common.testinfra.shouldMatchValue
import com.github.christophpickl.kpotpourri.http4k.non_test.WiremockTest
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo

class Http4kIntegrationTestes : WiremockTest() {

    private val mockStatusCode = 200
    private val mockResponseBody = "wiremock response body"
    private val mockBasePath = "/my"

    private val headerName = "X-http4k-test"
    private val headerValue = "testHeaderValue"

    // TODO test http4k without having baseUrl set doesnt combine URL

    // GET
    // =================================================================================================================

    fun `Given default Http4k and configured response, When GET, Then proper response object`() {
        stubFor(get(urlEqualTo(mockBasePath)).willReturn(
                aResponse()
                        .withStatus(mockStatusCode)
                        .withBody(mockResponseBody)))

        val response = defaultHttp4k.get(mockBasePath)

        assertThat(response, equalTo(Response4k(
                statusCode = mockStatusCode,
                bodyAsString = mockResponseBody,
                headers = response.headers // ignore headers by copying
        )))
    }

    fun `Given default Http4k, When GET with header, Then verify headers are set on request`() {
        wiremockStubGetCall()

        defaultHttp4k.get(mockBasePath) {
            headers += headerName to headerValue
        }

        verify(getRequestedFor(urlEqualTo(mockBasePath))
                .withHeader(headerName, WireMock.equalTo(headerValue))
        )
    }

    fun `Given default Http4k and wiremocked header, When GET, Then headers are set in response`() {
        wiremockStubGetCall() {
            withHeader(headerName, headerValue)
        }

        val response = defaultHttp4k.get(mockBasePath)

        // mapContains at least custom header, but additionally others from wiremock
        assertThat(response.headers, mapContains(headerName to headerValue))
        verify(getRequestedFor(urlEqualTo(mockBasePath)))
    }

    fun `Given default Http4k and wiremocked JSON response, When GET, Then JSON DTO should be marshalled`() {
        wiremockStubGetCall(responseBody = PersonDto.dummy.toJson())

        val actulJsonDto = defaultHttp4k.get(mockBasePath, PersonDto::class)

        actulJsonDto shouldMatchValue PersonDto.dummy
    }


    // POST
    // =================================================================================================================

    fun `Given default Http4k, When POST, Then should be received`() {
        stubFor(post(urlEqualTo(mockBasePath)))

        defaultHttp4k.post(mockBasePath)

        verify(postRequestedFor(urlEqualTo(mockBasePath)))
    }

    fun `Given default Http4k, When POST with JSON body, Then body should be received and content type set`() {
        stubFor(post(urlEqualTo(mockBasePath)))

        defaultHttp4k.post(mockBasePath) {
            requestBody = bodyJson(PersonDto.dummy)
        }

        verify(postRequestedFor(urlEqualTo(mockBasePath))
                .withRequestBody(WireMock.equalTo(PersonDto.dummy.toJson()))
                .withHeader("content-type", WireMock.equalTo("application/json"))
        )
    }

    fun `Given default Http4k, When POST with JSON body and custom content type, Then default content type should have been overridden`() {
        stubFor(post(urlEqualTo(mockBasePath)))

        defaultHttp4k.post(mockBasePath) {
            requestBody = bodyJson(PersonDto.dummy)
            headers += "content-type" to "application/foobar"
        }

        verify(postRequestedFor(urlEqualTo(mockBasePath))
                .withHeader("content-type", WireMock.equalTo("application/foobar")))
    }

    fun `Given default Http4k, When POST with JSON response, Then response DTO should be returned`() {
        stubFor(post(urlEqualTo(mockBasePath)).willReturn(aResponse().withBody(PersonDto.dummy.toJson())))

        val dto = defaultHttp4k.post(mockBasePath, PersonDto::class)

        dto shouldMatchValue PersonDto.dummy
    }

    private fun wiremockStubGetCall(
            basePath: String = mockBasePath,
            responseStatus: Int = mockStatusCode,
            responseBody: String = mockResponseBody,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}) {
        stubFor(get(urlEqualTo(basePath)).willReturn(
                aResponse()
                        .withStatus(responseStatus)
                        .withBody(responseBody)
                        .apply { withResponse(this) }))
    }

    private data class PersonDto(val name: String, val age: Int) {
        companion object {
            val dummy = PersonDto("Foobar", 42)
        }

        fun toJson() = """{"name":"$name","age":$age}"""
    }

}
