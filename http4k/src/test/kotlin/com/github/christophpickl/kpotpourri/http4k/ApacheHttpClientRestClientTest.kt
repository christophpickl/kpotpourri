package com.github.christophpickl.kpotpourri.http4k

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test


private const val WIREMOCK_PORT = 8042

@Test class ApacheHttpClientRestClientTest {

    // http://wiremock.org/docs/getting-started/
    private lateinit var server: WireMockServer

    @BeforeClass
    fun `wiremock startup`() {
        WireMock.configureFor("localhost", WIREMOCK_PORT)
        server = WireMockServer(wireMockConfig().port(WIREMOCK_PORT))
        server.start()
    }

    @AfterClass
    fun `wiremock stop`() {
        server.stop()
    }

    @BeforeMethod
    fun `wiremock reset`() {
        WireMock.reset()
    }

    fun `get returns status and body`() {
        stubFor(get(urlEqualTo("/foo"))
                .willReturn(
                        aResponse().withStatus(200).withBody("bar")
                )
        )

        val client = ApacheHttpClientRestClient()
        val response = client.execute(Request("http://localhost:$WIREMOCK_PORT/foo", HttpMethod.GET))
        println(response)
        assertThat(response, com.natpryce.hamkrest.equalTo(Response(200, "bar")))

        verify(getRequestedFor(urlEqualTo("/foo")))
    }
}
