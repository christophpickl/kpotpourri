package com.github.christophpickl.kpotpourri.wiremock4k.non_test

import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK4K_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK4K_HOSTNAME
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyGetRequest
import com.github.christophpickl.kpotpourri.wiremock4k.response.givenWiremock
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTestngListener
import org.testng.annotations.Listeners
import org.testng.annotations.Test


@Test
@Listeners(WiremockTestngListener::class)
class MyWiremockTest {

    fun `verify request`() {
        givenWiremock(WiremockMethod.GET, "/rest")

        println("execute request: http://$WIREMOCK4K_HOSTNAME:$DEFAULT_WIREMOCK4K_PORT/rest")

        verifyGetRequest("/rest")
    }

}
