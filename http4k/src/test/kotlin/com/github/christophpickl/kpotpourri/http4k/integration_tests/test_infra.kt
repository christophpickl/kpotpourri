package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.common.string.concatUrlParts
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.Http4kBuilder
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.SC_418_Teapot
import com.github.christophpickl.kpotpourri.http4k.StatusCode
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.internal.HttpImpl
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import org.testng.annotations.BeforeMethod

typealias HttpImplProducer = () -> HttpImpl
private val MOCK_PORT = 8043

abstract class Http4kWiremockTest(private val httpImpl: HttpImplProducer) : WiremockTest(port = MOCK_PORT) {

    companion object {
        val mockEndpointUrl = "/mock"

        val ANY_STATUS_CODE = SC_418_Teapot
        val ANY_RESPONSE_BODY = "wiremock response body"

        val headerName = "X-http4k-test"
        val headerValue = "testHeaderValue"
    }

    private val log = LOG {}
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
        overrideHttpImpl = httpImpl()
        withBuilder(this)
    }

    protected fun givenGetMockEndpointUrl(
            statusCode: StatusCode = SC_200_Ok,
            body: String? = null,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}
    ) {
        givenWiremock(
                method = WiremockMethod.GET,
                statusCode = statusCode,
                path = mockEndpointUrl,
                body = body,
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
    }

    fun toJson() = """{"name":"$name","age":$age}"""
}
