package com.github.christophpickl.kpotpourri.http4k.non_test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration


fun main(args: Array<String>) {
    withWiremock {
        stubFor(get(urlEqualTo("/my")).willReturn(
                        aResponse()
                                .withStatus(200)
                                .withBody("wiremock response body")))

        // execute HTTP4K ...


    }
}



fun withWiremock(port: Int = WIREMOCK_PORT, action: () -> Unit) {
    WireMock.configureFor("localhost", port)
    val server = WireMockServer(WireMockConfiguration.wireMockConfig().port(port))
    server.start()

    action()

    server.stop()
}
