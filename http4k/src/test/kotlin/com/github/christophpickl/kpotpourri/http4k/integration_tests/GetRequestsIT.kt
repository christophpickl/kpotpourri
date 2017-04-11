package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.test4k.mapContains
import com.github.christophpickl.kpotpourri.test4k.shouldMatchValue
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo


abstract class GetRequestsIT(restClient: RestClientProducer) : Http4kWiremockTest(restClient) {

    fun `Given default Http4k and configured response, When GET, Then proper response object`() {
        givenGetMockEndpointUrl(
                statusCode = ANY_STATUS_CODE,
                body = ANY_RESPONSE_BODY
        )

        val response = http4k.get(mockEndpointUrl)

        assertThat(response, equalTo(Response4k(
                statusCode = ANY_STATUS_CODE,
                bodyAsString = ANY_RESPONSE_BODY,
                headers = response.headers // ignore headers by copying
        )))
    }

    fun `Given default Http4k, When GET with header, Then verify headers are set on request`() {
        givenGetMockEndpointUrl()

        http4k.get(mockEndpointUrl) {
            headers += headerName to headerValue
        }

        verify(getRequestedFor(urlEqualTo(mockEndpointUrl))
                .withHeader(headerName, WireMock.equalTo(headerValue)))
    }

    fun `Given default Http4k and wiremocked header, When GET, Then headers are set in response`() {
        givenGetMockEndpointUrl {
            withHeader(headerName, headerValue)
        }

        val response = http4k.get(mockEndpointUrl)

        // mapContains at least custom header, but additionally others from wiremock
        assertThat(response.headers, mapContains(headerName to headerValue))
        verify(getRequestedFor(urlEqualTo(mockEndpointUrl)))
    }

    fun `Given default Http4k and wiremocked JSON response, When GET, Then JSON DTO should be marshalled`() {
        givenGetMockEndpointUrl(body = PersonDto.dummy.toJson())

        val actulJsonDto = http4k.get(mockEndpointUrl, PersonDto::class)

        actulJsonDto shouldMatchValue PersonDto.dummy
    }


}
