package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.internal.Request4k
import com.github.christophpickl.kpotpourri.http4k.internal.implementations.ApacheHttpClientRestClient
import com.github.christophpickl.kpotpourri.http4k.non_test.WIREMOCK_PORT
import com.github.christophpickl.kpotpourri.http4k.non_test.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

@Test class ApacheHttpClientRestClientTest : WiremockTest() {

    fun `get returns status and body`() {
        stubFor(get(urlEqualTo("/foo"))
                .willReturn(
                        aResponse().withStatus(200).withBody("bar")
                )
        )

        val client = ApacheHttpClientRestClient()
        val response = client.execute(Request4k("http://localhost:$WIREMOCK_PORT/foo", HttpMethod4k.GET))
        println(response)
        assertThat(response, com.natpryce.hamkrest.equalTo(Response4k(200, "bar")))

        verify(getRequestedFor(urlEqualTo("/foo")))
    }
}
