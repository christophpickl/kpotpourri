package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.fasterxml.jackson.core.type.TypeReference
import com.github.christophpickl.kpotpourri.http4k.Http4kException
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.http4k.non_test.toJson
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.mapContains
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.test4k.skip
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyGetRequest
import com.github.tomakehurst.wiremock.client.WireMock
import com.natpryce.hamkrest.assertion.assertThat


abstract class GetRequestsIT(restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    fun `Given default Http4k and configured response, When GET, Then proper response object`() {
        givenGetMockEndpointUrl(
                statusCode = anyStatusCode,
                body = anyResponseBody
        )

        val response = http4k.get<Response4k>(mockEndpointUrl)

        response shouldMatchValue Response4k(
                statusCode = anyStatusCode,
                bodyAsString = anyResponseBody,
                headers = response.headers // ignore headers by copying
        )
    }

    fun `Given default Http4k, When GET with header, Then verify headers are set on request`() {
        givenGetMockEndpointUrl()

        http4k.get<Any>(mockEndpointUrl) {
            headers += headerName to headerValue
        }

        verifyGetRequest(mockEndpointUrl) {
            withHeader(headerName, WireMock.equalTo(headerValue))
        }
    }

    fun `Given default Http4k and wiremocked header, When GET, Then headers are set in response`() {
        givenGetMockEndpointUrl {
            withHeader(headerName, headerValue)
        }

        val response = http4k.get<Response4k>(mockEndpointUrl)

        // mapContains at least custom header, but additionally others from wiremock
        assertThat(response.headers, mapContains(headerName to headerValue))

        verifyGetRequest(mockEndpointUrl)
    }

    fun `Given default Http4k and wiremocked JSON response, When GET, Then JSON DTO should be marshalled`() {
        givenGetMockEndpointUrl(body = PersonDto.dummy.toJson())

        val actulJsonDto = http4k.get<PersonDto>(mockEndpointUrl)

        actulJsonDto shouldMatchValue PersonDto.dummy
    }

    fun `getReturning with generic response type, Then Http4k should be so nice and indicate with exception`() {
        givenGetMockEndpointUrl(body = PersonDto.dummies.toJson())

        assertThrown<Http4kException>(listOf("type erasure", "TypeReference")) {
            http4k.getReturning(mockEndpointUrl, List::class)
        }
    }

    fun `get with generic response type, Then generic list should be marshalled`() {
        givenGetMockEndpointUrl(body = PersonDto.dummies.toJson())

        val actulJsonDto = http4k.get<List<PersonDto>>(mockEndpointUrl)

        actulJsonDto shouldMatchValue PersonDto.dummies
    }

    fun `getGeneric with generic response type, Then generic list should be marshalled`() {
        givenGetMockEndpointUrl(body = PersonDto.dummies.toJson())

        val actulJsonDto = http4k.getGeneric(mockEndpointUrl, object : TypeReference<List<PersonDto>>() {})

        actulJsonDto shouldMatchValue PersonDto.dummies
    }

    fun `Given default Http4k and wiremocke responses with list of persons, When GET, Then list should be returned`() {
        skip("WIP") // MINOR ex-FIX-ME implement me
        // java type erasure hick hack... mapper.readValue(jsonString, new TypeReference<List<PersonDto>>(){});
        val persons = listOf(PersonDto.dummy)
        givenGetMockEndpointUrl(body = persons.toJson())

        val actulJsonDto = http4k.get<List<PersonDto>>(mockEndpointUrl)

        actulJsonDto shouldMatchValue persons
    }

}
