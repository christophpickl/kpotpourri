package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK4K_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK4K_HOSTNAME
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import mu.KotlinLogging.logger
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Manages wiremock server startup/shutdown, and providing response preparation and request verification utilities.
 *
 * Needs to be a base class in order to provide more explicit functionality.
 */
@Test(groups = ["wiremock"])
@Deprecated(message = "use listener instead")
abstract class WiremockTest(
        private val port: Int = DEFAULT_WIREMOCK4K_PORT
) {

    private val log = logger {}

    /**
     * Default value: "http://localhost:9987"
     */
    protected val wiremockBaseUrl = "http://${WIREMOCK4K_HOSTNAME}:$port"

    // http://wiremock.org/docs/getting-started/
    protected lateinit var server: WireMockServer

    /** TestNG wiring to start wiremock. */
    @BeforeClass
    fun `wiremock startup`() {
        log.debug { "Starting up Wiremock on $wiremockBaseUrl for ${this::class.simpleName}" }
        WireMock.configureFor(WIREMOCK4K_HOSTNAME, port)
        server = WireMockServer(WireMockConfiguration.wireMockConfig().port(port))
        server.start()
    }

    /** TestNG wiring to stop wiremock. */
    @AfterClass
    fun `wiremock stop`() {
        log.debug { "Stopping wiremock server for ${this::class.simpleName}" }
        server.stop()
    }

    /** TestNG wiring to reset wiremock's state. */
    @BeforeMethod
    fun `wiremock reset`() {
        log.trace { "Reset wiremock" }
        WireMock.reset()
    }

}
