package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

const val WIREMOCK_HOSTNAME = "localhost"
const val WIREMOCK_PORT = 8042
const val WIREMOCK_DEFAULT_URL = "http://$WIREMOCK_HOSTNAME:$WIREMOCK_PORT"

@Test(groups = arrayOf("wiremock"))
abstract class WiremockTest(
        private val port: Int = WIREMOCK_PORT
) {

    companion object {
        protected val DEFAULT_STATUS_CODE = 200
        protected val DEFAULT_PATH = "/"
        protected val DEFAULT_METHOD = WiremockMethod.GET
    }

    // http://wiremock.org/docs/getting-started/
    protected lateinit var server: WireMockServer

    @BeforeClass
    fun `wiremock startup`() {
        WireMock.configureFor(WIREMOCK_HOSTNAME, port)
        server = WireMockServer(WireMockConfiguration.wireMockConfig().port(port))
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

    protected fun verifyWiremockGet(request: MockRequest) {
        verifyAnyRequest(request.toInternal(WiremockMethod.GET))
    }

    protected fun verifyPostRequest(request: MockRequest) {
        verifyAnyRequest(request.toInternal(WiremockMethod.POST))
    }

    internal fun verifyAnyRequest(request: InternalMockRequest) {
        val builder = request.method.requestedFor(request.path)
        request.func(builder)
        verify(builder)
    }

    /**
     * @param path relative URL, like "/my"
     */
    protected fun givenWiremock(
            method: WiremockMethod = DEFAULT_METHOD,
            path: String = DEFAULT_PATH,
            statusCode: Int = DEFAULT_STATUS_CODE,
            body: String? = null,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}) {
        stubFor(method.stubForPath(path).willReturn(
                aResponse()
                        .withStatus(statusCode)
                        .withBody(body)
                        .apply { withResponse(this) }))
    }

}


enum class WiremockMethod() {
    GET() {
        override fun stubForPath(path: String) = get(urlEqualTo(path))!!
        override fun requestedFor(path: String) = getRequestedFor(urlEqualTo(path))!!
    },
    POST() {
        override fun stubForPath(path: String) = post(urlEqualTo(path))!!
        override fun requestedFor(path: String) = postRequestedFor(urlEqualTo(path))!!
    }
    ;

    abstract fun stubForPath(path: String): MappingBuilder
    abstract fun requestedFor(path: String): RequestPatternBuilder
}


data class MockRequest(
        val path: String,
        val func: RequestPatternBuilder.() -> Unit = {}
) {
    internal fun toInternal(method: WiremockMethod) = InternalMockRequest(
            path = path,
            func = func,
            method = method
    )
}

/**
 * Adds a specific HTTP method to MockRequest.
 */
internal data class InternalMockRequest(
        val path: String,
        val func: RequestPatternBuilder.() -> Unit = {},
        val method: WiremockMethod
)
