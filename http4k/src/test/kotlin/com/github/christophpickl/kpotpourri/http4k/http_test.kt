package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.internal.HeadersMap
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.mapContainsExactly
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test class HeadersConfigTest {

    private lateinit var headers: HeadersConfig

    @BeforeMethod fun initHeaders() {
        headers = TestableHeadersConfig()
    }

    fun `acceptHeader - sunshine`() {
        headers.acceptHeader("foo")

        assertHeaders("Accept" to "foo")
    }

    private fun assertHeaders(vararg pairs: Pair<String, String>) {
        assertThat(headers.headers.map, mapContainsExactly(*pairs))
    }
}

class TestableHeadersConfig : HeadersConfig {
    override val headers = HeadersMap()
}
