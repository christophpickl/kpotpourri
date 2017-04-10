package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.string.concatUrlParts
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.mapContains
import com.github.christophpickl.kpotpourri.test4k.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_DEFAULT_URL
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_HOSTNAME
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.BeforeMethod

class Http4kIntegrationTestes : WiremockTest() {

    private lateinit var http4k: Http4k

    private val wiremockBaseUrl = BaseUrlByString(WIREMOCK_DEFAULT_URL)
    private val mockStatusCode = 200
    private val mockResponseBody = "wiremock response body"
    private val mockEndpointUrl = "/my"
    /** "http://localhost:8042/my" */
    private val mockWiremockUrlAndEndpointUrl = WIREMOCK_DEFAULT_URL + mockEndpointUrl

    private val headerName = "X-http4k-test"
    private val headerValue = "testHeaderValue"

    private val authUsername = "authUsername"
    private val authPassword = "authPassword"
    private val authHeaderValue = "Basic YXV0aFVzZXJuYW1lOmF1dGhQYXNzd29yZA=="


    @BeforeMethod
    fun `http4k re-set`() {
        http4k = buildHttp4k {
            baseUrl = wiremockBaseUrl
        }
    }

    // BASE URL
    // =================================================================================================================

    fun `Given Http4k without baseUrl, When request, Then URL was called`() {
        stubFor(get(urlEqualTo(mockEndpointUrl)))

        buildHttp4k { baseUrlDisabled() }
                .get(mockWiremockUrlAndEndpointUrl)

        verifyGetRequest(mockEndpointUrl)
    }

    fun `Given Http4k with baseUrl as config, When request, Then URL was called`() {
        stubFor(get(urlEqualTo(mockEndpointUrl)))

        buildHttp4k {
            baseUrlBy(UrlConfig(
                    protocol = HttpProtocol.Http,
                    hostName = WIREMOCK_HOSTNAME,
                    port = WIREMOCK_PORT
            ))
        }
                .get(mockEndpointUrl)

        verifyGetRequest(mockEndpointUrl)
    }

    fun `Given Http4k with baseUrl as string, When request, Then URL was called`() {
        stubFor(get(urlEqualTo(mockEndpointUrl)))

        buildHttp4k { baseUrlBy(WIREMOCK_DEFAULT_URL) }
                .get(mockEndpointUrl)

        verifyGetRequest(mockEndpointUrl)
    }

    // GET
    // =================================================================================================================

    fun `Given default Http4k and configured response, When GET, Then proper response object`() {
        stubFor(get(urlEqualTo(mockEndpointUrl)).willReturn(
                aResponse()
                        .withStatus(mockStatusCode)
                        .withBody(mockResponseBody)))

        val response = http4k.get(mockEndpointUrl)

        assertThat(response, equalTo(Response4k(
                statusCode = mockStatusCode,
                bodyAsString = mockResponseBody,
                headers = response.headers // ignore headers by copying
        )))
    }

    fun `Given default Http4k, When GET with header, Then verify headers are set on request`() {
        stubForSingle(
                method = WiremockMethod.GET,
                url = mockEndpointUrl)

        http4k.get(mockEndpointUrl) {
            headers += headerName to headerValue
        }

        verify(getRequestedFor(urlEqualTo(mockEndpointUrl))
                .withHeader(headerName, WireMock.equalTo(headerValue)))
    }

    fun `Given default Http4k and wiremocked header, When GET, Then headers are set in response`() {
        stubForSingle(
                method = WiremockMethod.GET,
                url = mockEndpointUrl) {
            withHeader(headerName, headerValue)
        }

        val response = http4k.get(mockEndpointUrl)

        // mapContains at least custom header, but additionally others from wiremock
        assertThat(response.headers, mapContains(headerName to headerValue))
        verify(getRequestedFor(urlEqualTo(mockEndpointUrl)))
    }

    fun `Given default Http4k and wiremocked JSON response, When GET, Then JSON DTO should be marshalled`() {
        stubForSingle(
                method = WiremockMethod.GET,
                url = mockEndpointUrl,
                body = PersonDto.dummy.toJson())

        val actulJsonDto = http4k.get(mockEndpointUrl, PersonDto::class)

        actulJsonDto shouldMatchValue PersonDto.dummy
    }


    // POST
    // =================================================================================================================

    fun `Given default Http4k, When POST, Then should be received`() {
        stubForSingle(method = WiremockMethod.POST)

        http4k.post(mockEndpointUrl)

        verify(postRequestedFor(urlEqualTo(mockEndpointUrl)))
    }

    fun `Given default Http4k, When POST with JSON body, Then body should be received and content type set`() {
        stubForSingle(method = WiremockMethod.POST, url = mockEndpointUrl)

        http4k.post(mockEndpointUrl) {
            requestBody = bodyJson(PersonDto.dummy)
        }

        verify(postRequestedFor(urlEqualTo(mockEndpointUrl))
                .withRequestBody(WireMock.equalTo(PersonDto.dummy.toJson()))
                .withHeader("content-type", WireMock.equalTo("application/json"))
        )
    }

    fun `Given default Http4k, When POST with JSON body and custom content type, Then default content type should have been overridden`() {
        stubFor(post(urlEqualTo(mockEndpointUrl)))

        http4k.post(mockEndpointUrl) {
            requestBody = bodyJson(PersonDto.dummy)
            headers += "content-type" to "application/foobar"
        }

        verify(postRequestedFor(urlEqualTo(mockEndpointUrl))
                .withHeader("content-type", WireMock.equalTo("application/foobar")))
    }

    fun `Given default Http4k, When POST with JSON response, Then response DTO should be returned`() {
        stubFor(post(urlEqualTo(mockEndpointUrl)).willReturn(aResponse().withBody(PersonDto.dummy.toJson())))

        val dto = http4k.post(mockEndpointUrl, PersonDto::class)

        dto shouldMatchValue PersonDto.dummy
    }

    // misc
    // =================================================================================================================

    fun `Given default Http4k, When GET with basic auth, Then Authorization header is set`() {
        stubForGetMockEndpointUrl()

        http4k.get(mockEndpointUrl) {
            basicAuth = BasicAuth(
                    username = authUsername,
                    password = authPassword
            )
        }

        verify(getRequestedFor(urlEqualTo(mockEndpointUrl))
                .withHeader("Authorization", WireMock.equalTo(authHeaderValue)))
    }

    fun `Given basic auth configured Http4k, When GET, Then Authorization header is set`() {
        stubForGetMockEndpointUrl()
        val http4k = buildHttp4k {
            basicAuth(authUsername, authPassword)
        }

        http4k.get(concatUrlParts(WIREMOCK_DEFAULT_URL, mockEndpointUrl))

        verify(getRequestedFor(urlEqualTo(mockEndpointUrl))
                .withHeader("Authorization", WireMock.equalTo(authHeaderValue)))
    }

    fun `Given basic auth configured Http4k, When GET with basic auth, Then Authorization header is set by request auth`() {
        stubForGetMockEndpointUrl()
        val http4k = buildHttp4k {
            basicAuth("some other", "some password")
        }

        http4k.get(concatUrlParts(WIREMOCK_DEFAULT_URL, mockEndpointUrl)) {
            basicAuth = BasicAuth(
                    username = authUsername,
                    password = authPassword
            )
        }

        verify(getRequestedFor(urlEqualTo(mockEndpointUrl))
                .withHeader("Authorization", WireMock.equalTo(authHeaderValue)))
    }

    // STATUS CODE CHECK
    // =================================================================================================================

    fun `Given global status check, When request status check also, Then request takes precedence`() {
        stubForGetMockEndpointUrl(status = SC_500_InternalError)
        val http4k = buildHttp4k {
            baseUrl = wiremockBaseUrl
            enforceStatusCode(SC_200_Ok)
        }

        http4k.get(mockEndpointUrl) {
            enforceStatusCode(SC_500_InternalError)
        }
    }

    fun `Given status 500, When check is disabled, Then do nothing`() {
        stubForGetMockEndpointUrl(status = SC_500_InternalError)

        http4k.get(mockEndpointUrl) {
            disableStatusCheck()
        }
    }

    fun `Given status 200, When check enforced to 200, Then do nothing`() {
        stubForGetMockEndpointUrl(status = SC_200_Ok)

        http4k.get(mockEndpointUrl) {
            enforceStatusCode(SC_200_Ok)
        }
    }

    fun `Given status 500, When check enforced to 200, Then throw exception`() {
        stubForGetMockEndpointUrl(status = SC_500_InternalError)

        assertThrown<Http4kStatusCodeException>({ e -> e.expected == SC_200_Ok && e.actual == SC_500_InternalError }) {
            http4k.get(mockEndpointUrl) {
                enforceStatusCode(SC_200_Ok)
            }
        }
    }

    fun `Given custom status check, When check succeeds, Then do nothing`() {
        stubForGetMockEndpointUrl(status = SC_200_Ok)

        http4k.get(mockEndpointUrl) {
            customStatusCheck { _, _ ->
                StatusCheckOk
            }
        }
    }

    fun `Given custom status check, When check fails, Then throw exception`() {
        val exceptionMessage = "testMessage"
        stubForGetMockEndpointUrl(status = SC_500_InternalError)

        assertThrown<Http4kStatusException>({ e -> e.message == exceptionMessage }) {
            http4k.get(mockEndpointUrl) {
                customStatusCheck { _, _ ->
                    StatusCheckFail(exceptionMessage)
                }
            }
        }
    }

    fun `Given status family check, When check succeeds, Then do nothing`() {
        stubForGetMockEndpointUrl(status = SC_200_Ok)

        http4k.get(mockEndpointUrl) {
            enforceStatusFamily(StatusFamily.Success_2)
        }
    }

    fun `Given status family check, When check fails, Then throw exception`() {
        stubForGetMockEndpointUrl(status = SC_500_InternalError)

        assertThrown<Http4kStatusException> {
            http4k.get(mockEndpointUrl) {
                enforceStatusFamily(StatusFamily.Success_2)
            }
        }
    }


    // QUERY PARAM
    // =================================================================================================================

    // FIXME implement me

    // helper
    // =================================================================================================================

    private enum class WiremockMethod() {
        GET() {
            override fun methodTranslation(url: String) = WireMock.get(urlEqualTo(url))!!
        },
        POST() {
            override fun methodTranslation(url: String) = WireMock.post(urlEqualTo(url))!!
        }
        ;

        abstract fun methodTranslation(url: String): MappingBuilder
    }

    private fun stubForGetMockEndpointUrl(status: StatusCode = mockStatusCode) {
        stubForSingle(status = status)
    }

    /**
     * @param url relative URL, like "/my"
     */
    private fun stubForSingle(
            method: WiremockMethod = WiremockMethod.GET,
            url: String = mockEndpointUrl,
            status: StatusCode = mockStatusCode,
            body: String = mockResponseBody,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}) {
        stubFor(method.methodTranslation(url).willReturn(
                aResponse()
                        .withStatus(status)
                        .withBody(body)
                        .apply { withResponse(this) }))
    }

    private data class PersonDto(
            // @get:JsonProperty("last_name")
            val name: String,
            val age: Int
    ) {
        companion object {
            val dummy = PersonDto("Foobar", 42)
        }

        fun toJson() = """{"name":"$name","age":$age}"""
    }

}
