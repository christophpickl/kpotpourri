package com.github.christophpickl.kpotpourri.wiremock4k.response

import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTest
import com.github.kittinunf.fuel.Fuel
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test


class PrepareResponseTest : WiremockTest() {

    fun `given - When requested, Then not throws`() {
        givenWiremock(WiremockMethod.GET, "/", 201, "response body")

        val (_, response, _) = Fuel.get(wiremockBaseUrl).response()
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
