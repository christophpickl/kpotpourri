package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.SC_418_Teapot
import com.github.christophpickl.kpotpourri.http4k.StatusCode
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_DEFAULT_URL
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import org.testng.annotations.BeforeMethod

abstract class Http4kWiremockTest : WiremockTest() {

    companion object {
        val mockEndpointUrl = "/my"

        val mockBaseUrl = WIREMOCK_DEFAULT_URL
        /** "http://localhost:8042/my" */
        val mockWiremockUrlAndEndpointUrl = WIREMOCK_DEFAULT_URL + mockEndpointUrl


        val ANY_PERSON = PersonDto.dummy
        val ANY_STATUS_CODE = SC_418_Teapot
        val ANY_RESPONSE_BODY = "wiremock response body"


        val headerName = "X-http4k-test"
        val headerValue = "testHeaderValue"
    }

    protected lateinit var http4k: Http4k

    @BeforeMethod
    fun `http4k re-set`() {
        http4k = buildHttp4k {
            baseUrlBy(mockBaseUrl)
        }
    }

    protected fun givenGetMockEndpointUrl(
            statusCode: StatusCode = SC_200_Ok,
            body: String? = null,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}
    ) {
        givenWiremock(
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
