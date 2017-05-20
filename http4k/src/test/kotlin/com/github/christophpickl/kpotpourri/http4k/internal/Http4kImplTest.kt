package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.GlobalHttp4kConfigurable
import com.github.christophpickl.kpotpourri.http4k.Http4kBuilder
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.http4k.non_test.testDummy
import com.github.christophpickl.kpotpourri.http4k.post
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.mapContains
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.mapContainsKey
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.not
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.assertion.assertThat
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import kotlin.reflect.KClass

@Test class Http4kImplTest {

    private val anyUrl = "anyUrl"
    private val anyBody = "anyBody"

    private lateinit var client: HttpClient
    private lateinit var globals: GlobalHttp4kConfigurable

    @BeforeMethod
    fun `init state`() {
        client = mock<HttpClient>()
        globals = Http4kBuilder()
    }

    fun `When GET, Then Content-Type should not be set`() {
        whenClientExecuteReturnsAnyResponse4k()

        testee().get<Any>(anyUrl)

        verify(client).execute(com.nhaarman.mockito_kotlin.check {
            assertThat(it.headers, not(mapContainsKey("Content-Type")))
        })
        verifyNoMoreInteractions(client)
    }

    fun `When set request body, Then Content-Type should be set to JSON`() {
        whenClientExecuteReturnsAnyResponse4k()

        testee().post<Any>(anyUrl) {
            requestBody(Dto("foo"))
        }

        verify(client).execute(com.nhaarman.mockito_kotlin.check {
            assertThat(it.headers, mapContains("Content-Type" to "application/json"))
        })
        verifyNoMoreInteractions(client)
    }

    fun <K, V> Map<K, V>.containsEntry(entry: Pair<K, V>) =
            containsKey(entry.first) && this[entry.first] == entry.second

    @DataProvider
    fun provideResponseCastings(): Array<Array<out Any?>> = arrayOf(
            arrayOf(anyBody, Response4k::class, null),
            arrayOf(anyBody, Any::class, null),
            arrayOf(anyBody, Unit::class, Unit),
            arrayOf("body", String::class, "body"),
            arrayOf("1.2", Float::class, (1.2).toFloat()),
            arrayOf("1.2", Double::class, (1.2).toDouble()),
            arrayOf("1", Byte::class, 1.toByte()),
            arrayOf("1", Short::class, 1.toShort()),
            arrayOf("1", Int::class, 1),
            arrayOf("1", Long::class, 1.toLong()),
            arrayOf("1", Boolean::class, true),
            arrayOf("true", Boolean::class, true),
            arrayOf("TRUE", Boolean::class, true),
            arrayOf("""{"name":"foo"}""", Dto::class, Dto("foo"))
    )

    data class Dto(val name: String)

    @Test(dataProvider = "provideResponseCastings")
    fun `Specifying response type`(givenBody: String, castTo: KClass<*>, expected: Any?) {
        val response = Response4k.testDummy.copy(bodyAsString = givenBody)
        whenever(client.execute(any())).thenReturn(response)

        val actualResponse = testee().getReturning(anyUrl, castTo)

        if (expected == null) { // null defaults to Response4k
            actualResponse shouldMatchValue response
        } else {
            actualResponse shouldMatchValue expected
        }
        verify(client).execute(any())
        verifyNoMoreInteractions(client)
    }

    private fun whenClientExecuteReturnsAnyResponse4k() {
        whenever(client.execute(any())).thenReturn(Response4k.testDummy)
    }

    private fun testee() = Http4kImpl(client, globals)

}
