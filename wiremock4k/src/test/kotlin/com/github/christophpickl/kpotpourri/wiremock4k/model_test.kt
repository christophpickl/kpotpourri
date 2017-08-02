package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class WiremockMethodTest {

    private val path = "testPath"

    @DataProvider
    fun provideMethods(): Array<Array<out Any>> = WiremockMethod.values().map { arrayOf(it) }.toTypedArray()

    @Test(dataProvider = "provideMethods")
    fun `stubForPath and requestedFor sunshine`(method: WiremockMethod) {
        method.stubForPath(path).build().request.method shouldMatchValue method.requestMethod
        method.requestedFor(path).build().method shouldMatchValue method.requestMethod
    }

}
