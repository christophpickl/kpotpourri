package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK4K_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK4K_HOSTNAME
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.testng.IClassListener
import org.testng.ITestClass
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult

/**
 * Injects the port used by wiremock server into a property like `private lateinit var port: Integer`.
 *
 * Attention: Kotlin's Int is a primitive type and therefor not usable with lateinit :-/
 */
@Target(AnnotationTarget.PROPERTY)
annotation class InjectMockPort

/**
 * Injects the base url "http://localhost:9987" used by wiremock server into a property like `private lateinit var mockUrl: String`.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class InjectMockUrl

/**
 * Specify explicit port to be used.
 */
@Target(AnnotationTarget.CLASS)
annotation class OverrideMockPort(val port: Int)

/**
 * Deals with starting/stopping/resetting the wiremock server.
 */
class WiremockTestngListener : IClassListener, ITestListener {

    private val log = LOG {}
    // FIXME inject base URL into tests
    private lateinit var server: WireMockServer
    private lateinit var wiremockBaseUrl: String
    private var port: Int? = null // actually lateinit, but as it is an Int... :-/

    /** Startup wiremock server. */
    override fun onBeforeClass(testClass: ITestClass) {
        val testInstance = testClass.testInstance()

        val overridePort = TestInitializer.exjectOverridePort(testInstance)
        port = overridePort ?: DEFAULT_WIREMOCK4K_PORT
        wiremockBaseUrl = "http://$WIREMOCK4K_HOSTNAME:$port"

        log.debug { "Starting up Wiremock on $wiremockBaseUrl for ${testInstance::class.simpleName}" }
        WireMock.configureFor(WIREMOCK4K_HOSTNAME, port!!)
        server = WireMockServer(WireMockConfiguration.wireMockConfig().port(port!!))
        server.start()

        TestInitializer.injectPort(testInstance, port!!)
        TestInitializer.injectMockUrl(testInstance, wiremockBaseUrl)
    }

    /** Shutdown wiremock server. */
    override fun onAfterClass(testClass: ITestClass) {
        log.debug { "Stopping wiremock server for ${testClass.testInstance()::class.simpleName}" }
        server.stop()
    }

    /** Reset mock's state. */
    override fun onTestStart(result: ITestResult) {
        log.trace { "Reset wiremock" }
        WireMock.reset()
    }

    /** Unused. */
    override fun onStart(context: ITestContext) {}

    /** Unused. */
    override fun onFinish(context: ITestContext) {}

    /** Unused. */
    override fun onTestSkipped(result: ITestResult) {}

    /** Unused. */
    override fun onTestSuccess(result: ITestResult) {}

    /** Unused. */
    override fun onTestFailure(result: ITestResult) {}

    /** Unused. */
    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) {}

    private fun ITestClass.testInstance(): Any {
        return getInstances(false)[0] // dont support @Factory
    }

}
