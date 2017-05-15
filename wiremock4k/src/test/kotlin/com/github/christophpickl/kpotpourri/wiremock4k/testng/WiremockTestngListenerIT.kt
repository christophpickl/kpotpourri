package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.test4k.skip
import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK4K_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK4K_HOSTNAME
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.response.givenWiremock
import com.github.kittinunf.fuel.Fuel
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test @Listeners(WiremockTestngListener::class)
class WiremockTestngListenerIT {

    fun `Wiremock server should have prepared response properly`() {
        givenWiremock(WiremockMethod.GET, "/", 200, "response body")

        val response = Fuel.get("http://${WIREMOCK4K_HOSTNAME}:${DEFAULT_WIREMOCK4K_PORT}/").response().second

        assertThat(response.httpStatusCode, equalTo(200))
        assertThat(String(response.data), equalTo("response body"))
    }

}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
@Test @Listeners(WiremockTestngListener::class)
class InjectMockPortIT {

    @InjectMockPort private lateinit var port: Integer

    fun `InjectMockPort - should be wiremock4k default port`() {
        port shouldMatchValue DEFAULT_WIREMOCK4K_PORT
    }

}

@Test @Listeners(WiremockTestngListener::class)
class InjectMockUrlIT {

    @InjectMockUrl private lateinit var mockUrl: String

    fun `InjectMockUrl - should be wiremock4k default host and default port`() {
        mockUrl shouldMatchValue "http://${WIREMOCK4K_HOSTNAME}:${DEFAULT_WIREMOCK4K_PORT}"
    }

}

private const val OVERRIDE_PORT = 8337

@Test @Listeners(WiremockTestngListener::class)
//@OverridePort(OVERRIDE_PORT)
class OverridePortIT {

    @InjectMockPort private lateinit var port: Integer

    fun `OverridePort - custom port received by InjectMockPort`() {
        skip("WIP")
        port shouldMatchValue OVERRIDE_PORT
    }

}

@Test @Listeners(WiremockTestngListener::class)
//@DynamicPort
class DynamicPortIT {

    @InjectMockPort private lateinit var port: Integer

    fun `DynamicPort - custom port received by InjectMockPort`() {
        skip("WIP")
        port shouldMatchValue OVERRIDE_PORT
    }

}
