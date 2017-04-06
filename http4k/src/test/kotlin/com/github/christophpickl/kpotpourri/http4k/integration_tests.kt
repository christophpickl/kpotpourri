package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.non_test.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo

class Http4kIntegrationTestes : WiremockTest() {

    private val mockStatusCode = 200
    private val mockResponseBody = "wiremock response body"
    private val mockBasePath = "/my"


    fun `Given default Http4k, When get URL, Then return response object`() {
        wiremockStubGetCall()

        val response = defaultHttp4k.get(mockBasePath)

        assertThat(response, equalTo(Response4k(
                statusCode = mockStatusCode,
                bodyAsString = mockResponseBody
        )))

        verify(getRequestedFor(urlEqualTo(mockBasePath)))
    }

    fun `Given default Http4k, When get URL with header, Then verify headers are set on request`() {
        wiremockStubGetCall()

        defaultHttp4k.get(mockBasePath) {
            headers += "foo" to "bar"
        }

        verify(getRequestedFor(urlEqualTo(mockBasePath))
                .withHeader("foo", WireMock.equalTo("bar"))
        )
    }

//        val responseDto = http4k.execute(
//                method = HttpMethod4k.POST,
//                url = "/my",
//                headers = listOf("a" to "b")
//                // queryGetParams
//                // cookies
//                // binary upload
//                // multi form
//                body = anyInstanceMarshalledViaJacksonsObjectMapper,
//                returnType = MyResponseDto.class
//        )


    private fun wiremockStubGetCall() {
        stubFor(get(urlEqualTo(mockBasePath)).willReturn(
                aResponse()
                        .withStatus(mockStatusCode)
                        .withBody(mockResponseBody)))
    }
}
