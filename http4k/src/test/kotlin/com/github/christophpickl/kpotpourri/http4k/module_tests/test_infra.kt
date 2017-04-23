package com.github.christophpickl.kpotpourri.http4k.module_tests

import com.github.christophpickl.kpotpourri.http4k.BodylessRequestOpts
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody
import com.github.christophpickl.kpotpourri.http4k.Http4kBuilder
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.StatusCode
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.internal.HttpImpl
import com.github.christophpickl.kpotpourri.http4k.toK2
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test abstract class ComponentTest {

    protected val testUrl = "testUrl"

    protected lateinit var httpMock: HttpImpl

    @BeforeMethod fun `init mock`() {
        httpMock = mock<HttpImpl>()
    }

    protected fun wheneverExecuteHttpMockReturnResponse(
            statusCode: StatusCode = SC_200_Ok,
            bodyAsString: String = "",
            headers: Map<String, String> = emptyMap()
    ) {
        whenever(httpMock.execute(any())).thenReturn(Response4k(statusCode, bodyAsString, headers))
    }

    protected fun wheneverExecuteHttpMockReturnResponse(response: Response4k) {
        whenever(httpMock.execute(any())).thenReturn(response)
    }

    protected fun http4kGet(withGlobals: Http4kBuilder.() -> Unit = {}, withRequest: BodylessRequestOpts.() -> Unit = {}) {
        val http4k = http4kWithMock(withGlobals)
        http4k.get<Any>(testUrl, withRequest)
    }

    protected fun http4kWithMock(withGlobals: Http4kBuilder.() -> Unit = {}) =
        buildHttp4k {
            overrideHttpImpl = httpMock
            withGlobals(this)
        }.toK2()

    protected fun verifyHttpMockExecutedWithRequest(
            method: HttpMethod4k = HttpMethod4k.GET,
            url: String = testUrl,
            headers: Map<String, String> = emptyMap(),
            requestBody: DefiniteRequestBody? = null
    ) {
        verify(httpMock).execute(Request4k(method, url, headers, requestBody))
    }
}
