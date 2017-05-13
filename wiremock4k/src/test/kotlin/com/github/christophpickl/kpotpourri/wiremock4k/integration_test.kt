package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.christophpickl.kpotpourri.wiremock4k.response.givenWiremock
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTest
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTestngListener
import com.github.kittinunf.fuel.Fuel
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Listeners
import org.testng.annotations.Test


@Test class WiremockTestIT : WiremockTest() {

    @BeforeClass
    fun beforre() {
        println("before: " + server)
    }

    @AfterClass
    fun foo() {
        println("before: " + server)
    }

    fun `server is running`() {
        assertThat(server.isRunning, equalTo(true))
    }
}


@Test
@Listeners(WiremockTestngListener::class)
class WiremockTestngListenerIT {

    // TODO @Inject private lateinit var port: Int

    fun `Wiremock server should have prepared response properly`() {
        givenWiremock(WiremockMethod.GET, "/", 200, "response body")

        val response = Fuel.get("http://$WIREMOCK_HOSTNAME:$DEFAULT_WIREMOCK_PORT/").response().second

        assertThat(response.httpStatusCode, equalTo(200))
        assertThat(String(response.data), equalTo("response body"))
    }

}
