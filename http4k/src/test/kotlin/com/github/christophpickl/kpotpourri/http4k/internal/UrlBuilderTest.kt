package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.web.QueryParams
import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.BaseUrlByString
import com.github.christophpickl.kpotpourri.http4k.GlobalHttp4kConfig
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class UrlBuilderTest {

    private val emptyParams = emptyMap<String, String>()

    @DataProvider
    fun provideUrlBuildings(): Array<Array<out Any?>> = arrayOf(
            arrayOf("", null, mapOf("request" to "1"), emptyParams, "?request=1"),
            arrayOf("", null, emptyParams, mapOf("global" to "1"), "?global=1"),
            arrayOf("", null, mapOf("request" to "1"), mapOf("global" to "2"), "?global=2&request=1"),
            arrayOf("", null, mapOf("key" to "request"), mapOf("key" to "global"), "?key=request"),

            arrayOf("url?u=0", null, mapOf("r" to "1"), mapOf("g" to "2"), "url?u=0&g=2&r=1"),
            arrayOf("url?key=inUrl", null, mapOf("key" to "request"), mapOf("key" to "global"), "url?key=request"),
            arrayOf("url?key=inUrl", null, emptyParams, mapOf("key" to "global"), "url?key=global"),
            arrayOf("url?key=inUrl", null, mapOf("key" to "request"), emptyParams, "url?key=request"),

            arrayOf("relativeUrl", "globalBaseUrl", emptyParams, emptyParams, "globalBaseUrl/relativeUrl")
    )

    @Test(dataProvider = "provideUrlBuildings")
    fun `buildUrl - sunshine`(url: String, givenBaseUrl: String?, requestParams: QueryParams, globalParams: QueryParams, expected: String) {
        val requestOpts = mock<AnyRequestOpts>()
        whenever(requestOpts.disableBaseUrl).thenReturn(givenBaseUrl == null)
        whenever(requestOpts.queryParams).thenReturn(requestParams.toMutableMap())

        val globalConfig = mock<GlobalHttp4kConfig>()
        if (givenBaseUrl != null) whenever(globalConfig.baseUrl).thenReturn(BaseUrlByString(givenBaseUrl))
        whenever(globalConfig.queryParams).thenReturn(globalParams.toMutableMap())

        buildUrl(url, globalConfig, requestOpts) shouldMatchValue expected
    }
}
