package com.github.christophpickl.kpotpourri.http4k.module_tests

import org.testng.annotations.Test

@Test class BaseUrlTest : ComponentTest() {

    private val testBaseUrl = "testBaseUrl"

    fun `baseUrl by string`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kGet(withGlobals = { baseUrlBy(testBaseUrl) })

        verifyHttpMockExecutedWithRequest(url = "$testBaseUrl/$testUrl")
    }

}
