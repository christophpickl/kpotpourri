package com.github.christophpickl.kpotpourri.web4k

import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

fun main(args: Array<String>) {
    println("MAIN... START")
    JettyServer(
            springConfig = DemoSpringConfig::class,
            port = TEST_PORT,
            servletPrefix = SERVLET_PREFIX).startInteractively()
    println("MAIN... END")
}

@Configuration
@Import(WebConfig::class)
class DemoSpringConfig {
    @Bean fun myResource() = MyResource()
}

private val TEST_PORT = 8442
private val SERVLET_PREFIX = "/test"
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

    @BeforeClass fun `start jetty`() {
        jetty = JettyServer(
                springConfig = DemoSpringConfig::class,
                port = TEST_PORT,
                servletPrefix = SERVLET_PREFIX)
        jetty.start()
    }

    @AfterClass fun `stop jetty`() {
        jetty.stop()
    }

    fun `make a GET request and proper response should have been returned`() {
        val http4k = buildHttp4k {
            baseUrlBy("http://localhost:$TEST_PORT$SERVLET_PREFIX")
        }

        val response = http4k.get<Response4k>("/")
        response shouldMatchValue Response4k(
                statusCode = SC_200_Ok,
                bodyAsString = DUMMY_RESPONSE,
                headers = response.headers // just copy them
        )
    }

}
