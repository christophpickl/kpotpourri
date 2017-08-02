package com.github.christophpickl.kpotpourri.wiremock4k.request

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsExactlyInOrder
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK4K_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.response.givenWiremock
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTestngListener
import com.github.kittinunf.fuel.Fuel
import com.github.tomakehurst.wiremock.client.VerificationException
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.HttpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test @Listeners(WiremockTestngListener::class)
class VerifyRequestTest {

    private val wiremockBaseUrl = "http://localhost:$DEFAULT_WIREMOCK4K_PORT" // FIXME change me

    fun `verifyWiremockGet - When not requested, Then throws`() {
        assertThrown<VerificationException> {
            verifyGetRequest(path = "/")
        }
    }

    fun `verifyGetRequest - When requested, Then not throws`() {
        givenWiremock(WiremockMethod.GET)
        Fuel.get(wiremockBaseUrl).response()
        verifyGetRequest(path = "/")
    }

    fun `verifyPostRequest - When requested, Then not throws`() {
        givenWiremock(WiremockMethod.POST)
        Fuel.post(wiremockBaseUrl).response()
        verifyPostRequest(path = "/")
    }

    fun `verifyPutRequest - When requested, Then not throws`() {
        givenWiremock(WiremockMethod.PUT)
        Fuel.put(wiremockBaseUrl).response()
        verifyPutRequest(path = "/")
    }

    fun `verifyDeleteRequest - When requested, Then not throws`() {
        givenWiremock(WiremockMethod.DELETE)
        Fuel.delete(wiremockBaseUrl).response()
        verifyDeleteRequest(path = "/")
    }

}

@Test class VerifyRequestExtensionsTest {

    private lateinit var request: Request

    @BeforeMethod fun initRequest() {
        request = mock<Request>()
    }

    fun `Request hasHeader - Given single value, Then has header`() {
        whenever(request.headers).thenReturn(HttpHeaders(HttpHeader("key", "val")))

        request.hasHeader("key" to "val") shouldMatchValue true
    }

    fun `Request hasHeader - Given multi value, Then has header`() {
        whenever(request.headers).thenReturn(HttpHeaders(HttpHeader("key", "skipped", "val")))

        request.hasHeader("key" to "val") shouldMatchValue true
    }

    fun `Request hasHeader - Given no header, Then not has header`() {
        whenever(request.headers).thenReturn(HttpHeaders())

        request.hasHeader("key" to "val") shouldMatchValue false
    }

    fun `Request hasHeader - Given wrong value, Then not has header`() {
        whenever(request.headers).thenReturn(HttpHeaders(HttpHeader("key", "different")))

        request.hasHeader("key" to "val") shouldMatchValue false
    }

    fun `RequestPatternBuilder withRequestBody - Sunshine`() {
        val bytes = byteArrayOf(0, 1, 1, 0)
        val builder = RequestPatternBuilder().withRequestBody(bytes)
        assertThat(builder.build().bodyPatterns, containsExactlyInOrder(WireMock.equalTo(String(bytes))))
    }

    fun `RequestPatternBuilder withHeader - Sunshine`() {
        val builder = RequestPatternBuilder().withHeader("key", "val")
        assertThat(builder.build().headers.get("key")!!.valuePattern, equalTo(WireMock.equalTo("val")))
    }

    fun `RequestPatternBuilder withCookie - Sunshine`() {
        val builder = RequestPatternBuilder().withCookie("key", "val")
        assertThat(builder.build().cookies.get("key"), equalTo(WireMock.equalTo("val")))
    }

}
