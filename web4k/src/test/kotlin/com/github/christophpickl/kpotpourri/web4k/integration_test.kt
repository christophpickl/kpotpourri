package com.github.christophpickl.kpotpourri.web4k

import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.web4k.ErrorHandlerType.CustomHandler
import com.github.christophpickl.kpotpourri.web4k.non_test.MyResource
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.testng.annotations.AfterClass
import org.testng.annotations.Test
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Configuration
@Import(WebConfig::class)
class DemoSpringConfig {
    @Bean fun myResource() = MyResource()
}

private val DUMMY_RESPONSE = "test dummy response"

@Path("/")
class MyResource {

    @GET
    @Path("/")
    @Produces("text/plain")
    fun getRoot() = DUMMY_RESPONSE

}

@Test class JettyServerIntegrationTest {

    private lateinit var jetty: JettyServer

    private val defaultConfig = JettyConfig(
            springConfig = DemoSpringConfig::class,
            port = 8449,
            servletPrefix = "/test"
    )

    private fun startJetty(config: JettyConfig) {
        jetty = JettyServer(config)
        jetty.start()
    }

    @AfterClass fun `stop jetty`() {
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
                bodyAsString = DUMMY_RESPONSE,
                headers = response.headers // just copy them
        )
    }

    fun `When adding custom filter, Then it should have caught the header`() {
        TestableHttpFilter.filtered.clear()
        startJetty(defaultConfig.copy(filters = listOf(TestableHttpFilter::class)))

        http4k().get<Response4k>("/")

        TestableHttpFilter.filtered.size shouldMatchValue 1
    }

    fun `custom error handler`() {
        val caughtErrors = mutableListOf<ErrorHandlingObject>()
        startJetty(defaultConfig.copy(errorHandler = CustomHandler(object : SpecificErrorHandler {
            override fun handle(error: ErrorHandlingObject) {
                caughtErrors.add(error)
            }
        })))

        http4k().get<Response4k>("/") {
            addHeader("accept" to "invalid")
        }

        assertThat(caughtErrors.size, equalTo(1))
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
