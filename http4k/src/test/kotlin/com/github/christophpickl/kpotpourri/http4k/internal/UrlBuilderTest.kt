package com.github.christophpickl.kpotpourri.http4k.internal

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class UrlBuilderTest {

    companion object {
        private val URL = "http://http4k.org"
        private val QUERY_KEY = "queryKey"
        private val QUERY_VAL = "queryVal"
        private val QUERY_KEY1 = QUERY_KEY + "1"
        private val QUERY_VAL1 = QUERY_VAL + "1"
        private val QUERY_KEY2 = QUERY_KEY + "2"
        private val QUERY_VAL2 = QUERY_VAL + "2"
    }

    fun `When no query params, Then return original URL`() {
        assertThat(
                UrlBuilder.build(URL, emptyMap()),
                equalTo(URL))
    }

    fun `When single query param set, Then append question mark and param`() {
        assertThat(
                UrlBuilder.build(URL, mapOf(QUERY_KEY to QUERY_VAL)),
                equalTo("$URL?$QUERY_KEY=$QUERY_VAL"))
    }

    fun `When two query params set, Then concat params with ampersand symbol`() {
        assertThat(
                UrlBuilder.build(URL, mapOf(QUERY_KEY1 to QUERY_VAL1, QUERY_KEY2 to QUERY_VAL2)),
                equalTo("$URL?$QUERY_KEY1=$QUERY_VAL1&$QUERY_KEY2=$QUERY_VAL2"))
    }

    fun `When query param set with ugly symbol, Then value should have been URL encoded`() {
        assertThat(
                UrlBuilder.build(URL, mapOf("key\"key" to "val=&val")),
                equalTo("$URL?key%22key=val%3D%26val"))
    }

}
