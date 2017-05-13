package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_HOSTNAME
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test


/**
 * Manages wiremock server startup/shutdown, and providing response preparation and request verification utilities.
 *
 * Needs to be a base class in order to provide more explicit functionality.
 */
@Test(groups = arrayOf("wiremock"))
abstract class WiremockTest(
        private val port: Int = DEFAULT_WIREMOCK_PORT
) {

    private val log = LOG {}

    /**
     * Default value: "http://localhost:9987"
     */
    protected val wiremockBaseUrl = "http://${WIREMOCK_HOSTNAME}:$port"

    // http://wiremock.org/docs/getting-started/
    protected lateinit var server: WireMockServer

    /** TestNG wiring to start wiremock. */
    @BeforeClass
    fun `wiremock startup`() {
        log.debug { "Starting up Wiremock on $wiremockBaseUrl" }
        WireMock.configureFor(WIREMOCK_HOSTNAME, port)
        server = WireMockServer(WireMockConfiguration.wireMockConfig().port(port))
        server.start()
    }

    /** TestNG wiring to stop wiremock. */
    @AfterClass
    fun `wiremock stop`() {
        server.stop()
    }

    /** TestNG wiring to reset wiremock's state. */
    @BeforeMethod
    fun `wiremock reset`() {
        WireMock.reset()
    }

}
