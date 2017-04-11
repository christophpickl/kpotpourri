package com.github.christophpickl.kpotpourri.http4k

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class Request4kTest {

    companion object {
        private val secret = "topsecret"
    }

    fun `toString does not render Authorization header value`() {
        assertRequestWithHeaderDoesNotContainSecret("Authorization")
    }

    fun `toString does not render authorization header value`() {
        assertRequestWithHeaderDoesNotContainSecret("authorization")
    }

    private fun assertRequestWithHeaderDoesNotContainSecret(headerName: String) {
        val requestToString = requestWithHeaderName(headerName).toString()

        assertThat("To string representation must not contain '$secret': $requestToString",
                requestToString.contains(secret), equalTo(false))
    }

    private fun requestWithHeaderName(name: String) = Request4k(headers = mapOf(name to secret), method = HttpMethod4k.GET, url = "")

}
