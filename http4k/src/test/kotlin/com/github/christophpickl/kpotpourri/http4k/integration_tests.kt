package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.non_test.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test(groups = arrayOf("wiremock")) class Http4kIntegrationTestes : WiremockTest() {

    fun `buildHttp4k and execute get should be ok`() {
        val mockStatusCode = 200
        val mockResponseBody = "wiremock response body"
        val mockBasePath = "/my"

        stubFor(get(urlEqualTo(mockBasePath)).willReturn(
                        aResponse()
                                .withStatus(mockStatusCode)
                                .withBody(mockResponseBody)))

        val http4k = buildHttp4k()
                .withDefaults {
                    baseUrl = wiremockBaseUrl
                }.end()

        val response: Response4k = http4k.get(mockBasePath, Response4k::class) {
            headers += "foo" to "bar" // TODO not yet implemented.
        }

        assertThat(response, equalTo(Response4k(
                statusCode = mockStatusCode,
                bodyAsString = mockResponseBody
        )))

        verify(getRequestedFor(urlEqualTo(mockBasePath)))
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
}
