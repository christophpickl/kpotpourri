package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK4K_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK4K_HOSTNAME
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import mu.KotlinLogging.logger
import org.testng.IClassListener
import org.testng.ITestClass
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult
import kotlin.reflect.full.findAnnotation

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
 * Tells wiremock to randomly select a port.
 */
@Target(AnnotationTarget.CLASS)
annotation class DynamicMockPort

/**
 * Deals with starting/stopping/resetting the wiremock server.
 */
class WiremockTestngListener : IClassListener, ITestListener {

    private val log = logger {}
    // MINOR inject base URL into tests
    private lateinit var server: WireMockServer
    private lateinit var wiremockBaseUrl: String
    private var port: Int? = null // actually lateinit, but as it is an Int... :-/

    /** Startup wiremock server. */
    override fun onBeforeClass(testClass: ITestClass) {
        val testInstance = testClass.testInstance()

        val overridePort = TestInitializer.exjectOverridePort(testInstance)
        val enableDynamicPort = testInstance::class.findAnnotation<DynamicMockPort>() != null

        port = when {
            overridePort != null && enableDynamicPort -> {
                log.warn { "Wiremock4k configured with custom port $overridePort and dynamic port usage enabled! Using custom port." }
                overridePort
            }
            overridePort != null -> overridePort
            else -> DEFAULT_WIREMOCK4K_PORT
        }

        server = WireMockServer(WireMockConfiguration.wireMockConfig().apply {
            if (enableDynamicPort) {
                dynamicPort()
            } else {
                port(port!!)
            }
        })
        server.start()
        if (enableDynamicPort) {
            port = server.port()
        }

        wiremockBaseUrl = "http://$WIREMOCK4K_HOSTNAME:$port"
        WireMock.configureFor(WIREMOCK4K_HOSTNAME, port!!)

        TestInitializer.injectPort(testInstance, port!!)
        TestInitializer.injectMockUrl(testInstance, wiremockBaseUrl)

        log.debug { "Started up Wiremock on $wiremockBaseUrl for ${testInstance::class.simpleName}" }
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
