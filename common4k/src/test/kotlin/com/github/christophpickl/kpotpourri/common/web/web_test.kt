package com.github.christophpickl.kpotpourri.common.web

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class WebTest {

    @DataProvider
    fun provideParsedUrls(): Array<Array<out Any>> = arrayOf(
            arrayOf("url", "url" to emptyMap<String, String>()),
            arrayOf("url?k=v", "url" to mapOf("k" to "v")),
            arrayOf("url?k1=v1&k2=v2", "url" to mapOf("k1" to "v1", "k2" to "v2"))
    )

    @Test(dataProvider = "provideParsedUrls")
    fun `parseUrl - sunshine`(url: String, expected: Pair<String, Map<String, String>>) {
        parseUrl(url) shouldMatchValue expected
    }

    @DataProvider
    fun provideConstructUrls(): Array<Array<out Any>> = arrayOf(
            arrayOf("", emptyMap<String, String>(), ""),
            arrayOf("", mapOf("k" to "v"), "?k=v"),
            arrayOf("", mapOf("k1" to "v1", "k2" to "v2"), "?k1=v1&k2=v2"),
            arrayOf("", mapOf("key\"key" to "val=&val"), "?key%22key=val%3D%26val"),
            arrayOf("?x=1", mapOf("k" to "v"), "?x=1&k=v"),
            arrayOf("?x=1&y=2", mapOf("k" to "v"), "?x=1&y=2&k=v")
    )

    @Test(dataProvider = "provideConstructUrls")
    fun `constructUrl - sunshine`(givenUrl: String, addParams: QueryParams, expectedUrl: String) {
        constructUrl(givenUrl, addParams) shouldMatchValue expectedUrl
    }

}
