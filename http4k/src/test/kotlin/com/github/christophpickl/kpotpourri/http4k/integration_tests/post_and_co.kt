package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.bodyJson
import com.github.christophpickl.kpotpourri.test4k.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.tomakehurst.wiremock.client.WireMock


class PostAndCoIT : Http4kWiremockTest() {

    // POST
    // =================================================================================================================

    fun `Given default Http4k, When POST, Then should be received`() {
        givenWiremock(method = WiremockMethod.POST, path = mockEndpointUrl)

        http4k.post(mockEndpointUrl)

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(mockEndpointUrl)))
    }

    fun `Given default Http4k, When POST with JSON body, Then body should be received and content type set`() {
        givenWiremock(method = WiremockMethod.POST, path = mockEndpointUrl)

        http4k.post(mockEndpointUrl) {
            requestBody = bodyJson(PersonDto.dummy)
        }

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(mockEndpointUrl))
                .withRequestBody(WireMock.equalTo(PersonDto.dummy.toJson()))
                .withHeader("content-type", WireMock.equalTo("application/json"))
        )
    }

    fun `Given default Http4k, When POST with JSON body and custom content type, Then default content type should have been overridden`() {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(mockEndpointUrl)))

        http4k.post(mockEndpointUrl) {
            requestBody = bodyJson(PersonDto.dummy)
            headers += "content-type" to "application/foobar"
        }

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(mockEndpointUrl))
                .withHeader("content-type", WireMock.equalTo("application/foobar")))
    }

    fun `Given default Http4k, When POST with JSON response, Then response DTO should be returned`() {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(mockEndpointUrl)).willReturn(WireMock.aResponse().withBody(PersonDto.dummy.toJson())))

        val dto = http4k.post(mockEndpointUrl, PersonDto::class)

        dto shouldMatchValue PersonDto.dummy
    }

    // TODO TEST explicitly for custom POST response type

}
