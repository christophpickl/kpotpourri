package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_HOSTNAME
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestNGListener
import org.testng.ITestResult

/**
 * Deals with starting/stopping/resetting the wiremock server.
 */
class WiremockTestngListener : ITestNGListener, ITestListener {

    private val log = LOG {}
    private val port: Int = DEFAULT_WIREMOCK_PORT
    private lateinit var server: WireMockServer

    /** Startup wiremock server. */
    override fun onStart(context: ITestContext) {
        log.debug { "onStart() ... starting wiremock server on port $port" }
        WireMock.configureFor(WIREMOCK_HOSTNAME, port)
        server = WireMockServer(WireMockConfiguration.wireMockConfig().port(port))
        server.start()
    }

    /** Shutdown wiremock server. */
    override fun onFinish(context: ITestContext) {
        log.debug { "onFinish() ... stopping wiremock server" }
        server.stop()
    }

    /** Reset mock's state. */
    override fun onTestStart(result: ITestResult) {
        WireMock.reset()
    }

    /** Unused. */
    override fun onTestSkipped(result: ITestResult) {}

    /** Unused. */
    override fun onTestSuccess(result: ITestResult) {}

    /** Unused. */
    override fun onTestFailure(result: ITestResult) {}

    /** Unused. */
    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) {}

}
