package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.reflection.Reflector
import com.github.christophpickl.kpotpourri.http4k.Http4kException
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test class HttpClientFactoryDetectorTest {

    private lateinit var reflector: Reflector

    @BeforeMethod
    fun `init mocks`() {
        reflector = mock<Reflector>()
    }

    fun `detect - Given no impls, Then throws`() {
        assertThrown<Http4kException>(expectedMessageParts = listOf("could not find any available implementation")) {
            testee().detect()
        }
    }

    fun `detect - Given apache impl, Then returns apache`() {
        whenever(reflector.lookupClass(HttpClientType.ApacheHttpClient.fqnToLookFor))
                .thenReturn(TestableHttpClientFactory::class.java)

        val actual = testee().detect()

        assertThat(actual is TestableHttpClientFactory, equalTo(true)) // stupid me
    }

    fun `detect - Given two impls, Then throws`() {
        whenever(reflector.lookupClass(HttpClientType.ApacheHttpClient.fqnToLookFor))
                .thenReturn(TestableHttpClientFactory::class.java)
        whenever(reflector.lookupClass(HttpClientType.FuelClient.fqnToLookFor))
                .thenReturn(TestableHttpClientFactory::class.java)

        assertThrown<Http4kException>(expectedMessageParts = listOf("Multiple implementations found")) {
            testee().detect()
        }
    }

    private fun testee() = HttpClientFactoryDetector(reflector)

    private class TestableHttpClientFactory : HttpClientFactory {
        override fun build(metaMap: MetaMap) = throw UnsupportedOperationException()

    }
}
