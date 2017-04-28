package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.delete
import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.patch
import com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.put
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

val WIREMOCK_HOSTNAME = "localhost"
val WIREMOCK_PORT = 8042
//const val WIREMOCK_DEFAULT_URL = "http://$WIREMOCK_HOSTNAME:$WIREMOCK_PORT"

// TODO get rid of baseclass and introduce TestNG listeners
@Test(groups = arrayOf("wiremock"))
abstract class WiremockTest(
        private val port: Int = WIREMOCK_PORT
) {

    companion object {
        // to access SC_200_Ok instead, it needs to be outsourced to sub-module first (to avoid cyclic deps)
        protected val DEFAULT_STATUS_CODE = 200
        protected val DEFAULT_PATH = "/"
        protected val DEFAULT_METHOD = WiremockMethod.GET
    }

    private val log = LOG {}

    /**
     * Default value: "http://localhost:8042"
     */
    protected val wiremockBaseUrl = "http://$WIREMOCK_HOSTNAME:$port"

    // http://wiremock.org/docs/getting-started/
    protected lateinit var server: WireMockServer

    @BeforeClass
    fun `wiremock startup`() {
        log.debug { "Starting up Wiremock on $wiremockBaseUrl" }
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
        verifyRequest(request.toInternal(WiremockMethod.GET))
    }

    protected fun verifyPostRequest(request: MockRequest) {
        verifyRequest(request.toInternal(WiremockMethod.POST))
    }

    protected fun verifyPutRequest(request: MockRequest) {
        verifyRequest(request.toInternal(WiremockMethod.PUT))
    }

    protected fun verifyDeleteRequest(request: MockRequest) {
        verifyRequest(request.toInternal(WiremockMethod.DELETE))
    }

    protected fun verifyRequest(
            method: WiremockMethod,
            path: String,
            func: RequestPatternBuilder.() -> Unit = {}
    ) {
        verifyRequest(InternalMockRequest(path, func, method))
    }

    protected fun verifyRequest(request: InternalMockRequest) {
        val builder = request.method.requestedFor(request.path)
        request.func(builder)
        verify(1, builder)
    }

    /**
     * @param path relative URL, like "/my"
     */
    protected fun givenWiremock(
            method: WiremockMethod = /*GET*/DEFAULT_METHOD,
            path: String = /* "/" */DEFAULT_PATH,
            statusCode: Int = /*200*/DEFAULT_STATUS_CODE,
            /*response*/ body: String? = null,
            withResponse: ResponseDefinitionBuilder.() -> Unit = {}) {
        log.debug { "given wirmock for  ${method.name} $path (status=$statusCode, body=...)" }
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
    },
    PUT() {
        override fun stubForPath(path: String) = put(urlEqualTo(path))!!
        override fun requestedFor(path: String) = putRequestedFor(urlEqualTo(path))!!
    },
    DELETE() {
        override fun stubForPath(path: String) = delete(urlEqualTo(path))!!
        override fun requestedFor(path: String) = deleteRequestedFor(urlEqualTo(path))!!
    },
    PATCH() {
        override fun stubForPath(path: String) = patch(urlEqualTo(path))!!
        override fun requestedFor(path: String) = patchRequestedFor(urlEqualTo(path))!!
    }
    ;

    /** For preparing. */
    abstract fun stubForPath(path: String): MappingBuilder

    /** For verification. */
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
data class InternalMockRequest(
        val path: String,
        val func: RequestPatternBuilder.() -> Unit = {},
        val method: WiremockMethod
)
