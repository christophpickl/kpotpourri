package com.github.christophpickl.kpotpourri.http4k.internal

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class UrlBuilderTest {

    companion object {
        private val URL = "http://http4k.org"
    }

    fun `When no query params, Then return original URL`() {
        assertThat(
                UrlBuilder.build(URL, emptyMap()),
                equalTo(URL))
    }

    fun `When single query param set, Then append question mark and param`() {
        assertThat(
                UrlBuilder.build(URL, mapOf("k" to "v")),
                equalTo("$URL?k=v"))
    }

    fun `When two query params set, Then concat params with ampersand symbol`() {
        assertThat(
                UrlBuilder.build(URL, mapOf("k1" to "v1", "k2" to "v2")),
                equalTo("$URL?k1=v1&k2=v2"))
    }

    fun `When query param set with ugly symbol, Then value should have been URL encoded`() {
        assertThat(
                UrlBuilder.build(URL, mapOf("key\"key" to "val=&val")),
                equalTo("$URL?key%22key=val%3D%26val"))
    }

    fun `Given URL already got one query param set, When query param is set as well, Then combine both properly`() {
        assertThat(
                UrlBuilder.build(URL + "?k1=v1", mapOf("k2" to "v2")),
                equalTo("$URL?k1=v1&k2=v2"))
    }

    fun `Given URL already got two query param set, When two query params are set as well, Then combine all properly`() {
        assertThat(
                UrlBuilder.build(URL + "?k1=v1&k1b=v1b", mapOf("k2" to "v2", "k2b" to "v2b")),
                equalTo("$URL?k1=v1&k1b=v1b&k2=v2&k2b=v2b"))
    }
}
