package com.github.christophpickl.kpotpourri.http4k.non_test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration


fun withWiremock(port: Int = WIREMOCK_PORT, action: () -> Unit) {
    WireMock.configureFor("localhost", WIREMOCK_PORT)
    val server = WireMockServer(WireMockConfiguration.wireMockConfig().port(WIREMOCK_PORT))
    server.start()

    action()

    server.stop()
}

fun main(args: Array<String>) {
    /*
    withWiremock {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/my"))
                .willReturn(
                        WireMock.aResponse().withStatus(200).withBody("wiremock response body")
                )
        )

        val http4k = buildHttp4k()
                .withDefaults {
                    baseUrl = baseUrlBy("http://localhost:$WIREMOCK_PORT")
                }.end()
        val response = http4k.get("/my") {
            headers += "foo" to "bar"
        }
        println("response: $response")
    }
    */
    /*
    val factory = Http4kFactory()

    val http4k = factory
            .withDefaults {
                baseUrl = "http://localhost:8080"
            }
            .build()
    val someDto = http4k.get("/my") {
        headers += "asdf" to "by"
        queryParameters += "asdf" to "by"
        returnType = SomeDto.class
    }
    */
}
