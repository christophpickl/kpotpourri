package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

const val WIREMOCK_HOSTNAME = "localhost"
const val WIREMOCK_PORT = 8042
const val WIREMOCK_DEFAULT_URL = "http://$WIREMOCK_HOSTNAME:$WIREMOCK_PORT"

@Test(groups = arrayOf("wiremock"))
abstract class WiremockTest {

    // http://wiremock.org/docs/getting-started/
    protected lateinit var server: WireMockServer

    @BeforeClass
    fun `wiremock startup`() {
        WireMock.configureFor(WIREMOCK_HOSTNAME, WIREMOCK_PORT)
        server = WireMockServer(WireMockConfiguration.wireMockConfig().port(WIREMOCK_PORT))
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

    protected fun verifyGetRequest(url: String) {
        verify(getRequestedFor(urlEqualTo(url)))
    }

}
