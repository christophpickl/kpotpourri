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

    protected fun verifyGetRequest(url: String) {
        verify(getRequestedFor(urlEqualTo(url)))
    }

    protected fun verifyPostRequest(url: String, func: RequestPatternBuilder.() -> Unit = {}) {
        val builder = postRequestedFor(urlEqualTo(url))
        func(builder)
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
        stubFor(method.methodTranslation(path).willReturn(
                aResponse()
                        .withStatus(statusCode)
                        .withBody(body)
                        .apply { withResponse(this) }))
    }

}


enum class WiremockMethod() {
    GET() {
        override fun methodTranslation(path: String) = get(urlEqualTo(path))!!
    },
    POST() {
        override fun methodTranslation(path: String) = post(urlEqualTo(path))!!
    }
    ;

    abstract fun methodTranslation(path: String): MappingBuilder
}
