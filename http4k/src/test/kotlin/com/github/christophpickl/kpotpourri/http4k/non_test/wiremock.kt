package com.github.christophpickl.kpotpourri.http4k.non_test

import com.github.christophpickl.kpotpourri.http4k.BaseUrl.BaseUrlByString
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
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

    protected val wiremockBaseUrl = BaseUrlByString(WIREMOCK_DEFAULT_URL)

    // http://wiremock.org/docs/getting-started/
    protected lateinit var server: WireMockServer

    protected lateinit var defaultHttp4k: Http4k

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

    @BeforeMethod
    fun `defaultHttp4k re-set`() {
        defaultHttp4k = buildHttp4k {
            // MINOR architectural glitch?! :-/
//            withDefaults {
                baseUrl = wiremockBaseUrl
//            }
        }
    }

    protected fun verifyGetRequest(url: String) {
        verify(getRequestedFor(urlEqualTo(url)))
    }

}
