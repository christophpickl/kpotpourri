package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.response.givenWiremock
import com.github.christophpickl.kpotpourri.wiremock4k.testng.InjectMockPort
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTestngListener
import com.github.kittinunf.fuel.Fuel
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Listeners
import org.testng.annotations.Test


@Test
@Listeners(WiremockTestngListener::class)
// FIXME @OverridePort(8337)
// TODO @DynamicPort
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class WiremockTestngListenerIT {

    @InjectMockPort private lateinit var port: Integer

    fun `injected port should be wiremock4k default port`() {
        port shouldMatchValue DEFAULT_WIREMOCK4K_PORT
    }

    fun `Wiremock server should have prepared response properly`() {
        givenWiremock(WiremockMethod.GET, "/", 200, "response body")

        val response = Fuel.get("http://$WIREMOCK4K_HOSTNAME:$DEFAULT_WIREMOCK4K_PORT/").response().second

        assertThat(response.httpStatusCode, equalTo(200))
        assertThat(String(response.data), equalTo("response body"))
    }

}

