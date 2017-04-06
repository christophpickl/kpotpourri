package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.non_test.WiremockTest
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.describe
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
                bodyAsString = mockResponseBody,
                headers = response.headers // ignore headers for this test
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

    fun `Given default Http4k and wiremocked header, When get URL, Then headers are set in response`() {
        val headerName = "X-http4k-test"
        val headerValue = "testHeaderValue"
        wiremockStubGetCall() {
            withHeader(headerName, headerValue)
        }

        val response = defaultHttp4k.get(mockBasePath)

        // mapContains at least custom header, but additionally others from wiremock
        assertThat(response.headers, mapContains(headerName to headerValue))
        verify(getRequestedFor(urlEqualTo(mockBasePath)))
    }

//        val responseDto = http4k.execute(
//                method = HttpMethod4k.POST,
//                // queryGetParams
//                // cookies

//                returnType = MyResponseDto.class
//        )


    private fun wiremockStubGetCall(withResponse: ResponseDefinitionBuilder.() -> Unit = {}) {
        stubFor(get(urlEqualTo(mockBasePath)).willReturn(
                aResponse()
                        .withStatus(mockStatusCode)
                        .withBody(mockResponseBody)
                        .apply { withResponse(this) }))
    }
}

fun <K, V> mapContains(entry: Pair<K, V>): Matcher<Map<K, V>> = object : Matcher.Primitive<Map<K, V>>() {
    override fun invoke(actual: Map<K, V>): MatchResult {
        return if (actual.containsKey(entry.first) && actual[entry.first] == entry.second) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch("was ${describe(actual)}")
        }
    }
    override val description: String get() = "contains ${describe(entry)}"
    override val negatedDescription: String get() = "does not contain ${describe(entry)}"
}
