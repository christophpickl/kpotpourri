package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.common.string.concatUrlParts
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.Http4kBuilder
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.SC_418_Teapot
import com.github.christophpickl.kpotpourri.http4k.StatusCode
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClient
import com.github.christophpickl.kpotpourri.jackson4k.asString
import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4kMapper
import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK4K_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.response.givenWiremock
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTest
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import mu.KotlinLogging.logger
import org.testng.annotations.BeforeMethod

typealias HttpImplProducer = () -> HttpClient

val testMapper = buildJackson4kMapper()

abstract class Http4kWiremockTest(
        private val httpImpl: HttpImplProducer,
        port: Int = DEFAULT_WIREMOCK4K_PORT
) : WiremockTest(port) {

    protected val mockEndpointUrl = "/mock"
    protected val anyStatusCode = SC_418_Teapot
    protected val anyResponseBody = "wiremock response body"
    protected val headerName = "X-http4k-test"
    protected val headerValue = "testHeaderValue"

    private val log = logger {}
    /** "http://localhost:8042/my" */
    protected val mockWiremockUrlAndEndpointUrl = concatUrlParts(wiremockBaseUrl, mockEndpointUrl)
    protected lateinit var http4k: Http4k

    @BeforeMethod
    fun `before method, reset http4k instance with base URL and override http implementation`() {
        http4k = buildCustomHttp4k()
    }

    protected fun buildCustomHttp4k(withBuilder: Http4kBuilder.() -> Unit = {}) = buildHttp4k {
        log.trace { "buildCustomHttp4k()" }
        baseUrlBy(wiremockBaseUrl)
        overrideHttpClient = httpImpl()
        withBuilder(this)
    }

    protected fun givenGetMockEndpointUrl(
            statusCode: StatusCode = SC_200_Ok,
            body: String? = null,
            path: String = mockEndpointUrl,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}
    ) {
        givenWiremock(
                method = WiremockMethod.GET,
                statusCode = statusCode,
                path = path,
                responseBody = body,
                withResponse = withResponse
        )
    }

}

data class PersonDto(
        // @get:JsonProperty("last_name")
        val name: String,
        val age: Int
) {
    companion object {
        val dummy = PersonDto("dummy", 42)
        val dummy1 = PersonDto("dummy1", 1)
        val dummy2 = PersonDto("dummy2", 2)

        val dummies = listOf(dummy1, dummy2)
    }

    fun toJson() = testMapper.asString(this)
}

fun List<Any>.toJson() = testMapper.asString(this)
