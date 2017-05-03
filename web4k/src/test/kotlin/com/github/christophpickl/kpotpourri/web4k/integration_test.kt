package com.github.christophpickl.kpotpourri.web4k

import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.SC_400_BadRequest
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.mapContains
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.web4k.ErrorHandlerType.Custom
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Configuration
@Import(WebConfig::class)
class TestableSpringConfig {
    @Bean fun myResource() = TestableResource()
}

private val TESTABLE_RESPONSE = "testable response"

@Path("/")
class TestableResource {

    @GET
    @Path("/")
    @Produces("text/plain")
    fun getRoot() = TESTABLE_RESPONSE

}

@Test class JettyServerIntegrationTest {

    private lateinit var jetty: JettyServer

    private val defaultConfig = JettyConfig(
            springConfig = TestableSpringConfig::class,
            port = 8449,
            servletPrefix = "/test"
    )

    private fun startJetty(config: JettyConfig) {
        jetty = JettyServer(config)
        jetty.start()
    }

    @AfterMethod fun stopJetty() {
        if (jetty.isRunning) {
            jetty.stop()
        }
    }

    private fun http4k() = buildHttp4k {
        baseUrlBy(jetty.fullUrl)
    }

    fun `make a GET request and proper response should have been returned`() {
        startJetty(defaultConfig)

        val response = http4k().get<Response4k>("/")

        response shouldMatchValue Response4k(
                statusCode = SC_200_Ok,
                bodyAsString = TESTABLE_RESPONSE,
                headers = response.headers // just copy them
        )
    }

    fun `When adding custom filter, Then it should have caught the header`() {
        TestableHttpFilter.filtered.clear()
        startJetty(defaultConfig.copy(filters = listOf(TestableHttpFilter::class)))

        http4k().get<Response4k>("/")

        TestableHttpFilter.filtered.size shouldMatchValue 1
    }

    fun `error handler, Given handler set to custom, When invalid request sent, Then error should have been propagated`() {
        val caughtErrors = mutableListOf<ErrorObject>()
        startJetty(defaultConfig.copy(errorHandler = Custom(object : CustomErrorHandler {
            override fun handle(error: ErrorObject) {
                caughtErrors.add(error)
            }
        })))

        anyInvalidHttpRequest()

        assertThat(caughtErrors.size, equalTo(1))
    }

    fun `error handler, Given handler set to JSON, When invalid header sent, Then JSON object should have been rendered`() {
        startJetty(defaultConfig.copy(errorHandler = ErrorHandlerType.Json))

        val response = http4k().get<Response4k>("/") {
            addHeader("accept" to "invalid")
        }
        val errorResponse = response.readJson(ErrorResponse::class)

        assertThat(response.statusCode, equalTo(SC_400_BadRequest))
        assertThat(response.headers, mapContains("Content-Type" to "application/json"))
        assertThat(errorResponse, equalTo(ErrorResponse(
                requestMethod = "GET",
                requestUrl = jetty.fullUrl,
                statusCode = 400,
                errorMessage = "Bad Request",
                message = "Bad Request",
                stackTrace = null
        )))
    }

    // MINOR test for stack trace enabled


    // MINOR test for default error handler

    private fun anyInvalidHttpRequest() =
            http4k().get<Response4k>("/") {
                addHeader("accept" to "invalid")
            }

}

class TestableHttpFilter : HttpFilter() {
    companion object {
        // static hack, as jetty requires us to use class references rather than object references
        val filtered: MutableList<Pair<HttpServletRequest, HttpServletResponse>> = mutableListOf()
    }

    override fun doHttpFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        chain.doFilter(request, response)
        filtered.add(request to response)
    }

}
