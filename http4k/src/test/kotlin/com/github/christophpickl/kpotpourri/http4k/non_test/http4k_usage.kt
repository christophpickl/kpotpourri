package com.github.christophpickl.kpotpourri.http4k.non_test

import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.StatusFamily
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.toK2
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_PORT
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


        val http4k = buildHttp4k {
            baseUrlBy("http://some.server/rest")
            addHeader("someConstant" to "headerValue")
            addQueryParam("sort" to "name")
            enforceStatusFamily(StatusFamily.Success_2)
        }.toK2()

        val response: Response4k = http4k.get("/resource") {
            addHeader("Accept" to "my/content")
            addQueryParam("sort" to "asc")
        }
        println("read: $response")

        val dto: SomeDto = http4k.get("/resource") {
            addHeader("Accept" to "my/content")
            addQueryParam("sort" to "asc")
        }
        println("read: $dto")

//        response.bodyAsString
//        response.headers
        if (response.statusCode != SC_200_Ok) {
            // do something
        }
    }
}

data class SomeDto (val string: String)


fun withWiremock(port: Int = WIREMOCK_PORT, action: () -> Unit) {
    WireMock.configureFor("localhost", port)
    val server = WireMockServer(WireMockConfiguration.wireMockConfig().port(port))
    server.start()

    action()

    server.stop()
}
