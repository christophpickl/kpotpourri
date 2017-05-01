package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.tomakehurst.wiremock.http.HttpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import com.github.tomakehurst.wiremock.http.Request
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test class ExtensionsTest {

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

}
