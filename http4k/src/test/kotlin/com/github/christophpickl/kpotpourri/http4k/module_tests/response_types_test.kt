package com.github.christophpickl.kpotpourri.http4k.module_tests

import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.Test
import kotlin.reflect.KClass

@Test class ResponseTypesTest : ComponentTest() {

    private val responseBody = "responseBody"

    fun `When get response type String, Then return response body as string`() {
        assertResponse(String::class, responseBody, responseBody)
    }

    fun `When get response type Response4k, Then return Response4k`() {
        assertResponse(Response4k::class, responseBody, Response4k(SC_200_Ok, responseBody))
    }

    fun `When get response type Any, Then return Response4k`() {
        assertResponse(Any::class, responseBody, Response4k(SC_200_Ok, responseBody))
    }

    fun `When get response type Unit, Then return Unit`() {
        assertResponse(Unit::class, "", Unit)
    }

    fun `Given 1 as response body, When get response type Int, Then return 1`() {
        assertResponse(Int::class, "1", 1)
    }

    fun `Given non number response body, When get response type Int, Then exception thrown`() {
        wheneverExecuteHttpMockReturnResponse(bodyAsString = "a")

        assertThrown<NumberFormatException> {
            http4kWithMock().get(testUrl, Int::class)
        }
    }

    fun `Given non number response body, When get response type Boolean, Then return fail`() {
        assertResponse(Boolean::class, "true", true)
    }

    private fun <R : Any> assertResponse(returnType: KClass<R>, body: String, expected: Any) {
        wheneverExecuteHttpMockReturnResponse(bodyAsString = body)
        val response = http4kWithMock().get(testUrl, returnType)
        response shouldMatchValue expected
    }

}
