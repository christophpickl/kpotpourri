package com.github.christophpickl.kpotpourri.wiremock4k.response

import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.testng.InjectMockPort
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTestngListener
import com.github.kittinunf.fuel.Fuel
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test @Listeners(WiremockTestngListener::class)
class PrepareResponseTest {

    // FIXME @InjectWiremockUrl private lateinit var wiremockUrl: String
    @InjectMockPort private lateinit var port: Integer

    fun `given - When requested, Then not throws`() {
        givenWiremock(WiremockMethod.GET, "/", 201, "response body")

        val (_, response, _) = Fuel.get("http://localhost:$port").response()
        assertThat(String(response.data), equalTo("response body"))
        assertThat(response.httpStatusCode, equalTo(201))
    }

}

@Test class PrepareResponseExtensionsTest {

    fun `ResponseDefinitionBuilder withHeaders - sunshine`() {
        assertThat(ResponseDefinitionBuilder().withHeaders("a" to "1").build().headers.getHeader("a").values()[0],
                equalTo("1"))
    }

}
