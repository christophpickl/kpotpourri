package com.github.christophpickl.kpotpourri.http4k.module_tests

import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody
import com.github.christophpickl.kpotpourri.http4k.Http4k2
import com.github.christophpickl.kpotpourri.http4k.HttpBodyfullMethod4k
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.Test

@Test class Http4k2Test : ComponentTest() {

    private val dto = Dto.testee

    fun `get with opts`() {
        wheneverExecuteHttpMockReturnResponse()
        val http4k = http4kWithMock()

        Http4k2(http4k).get<Response4k>("url") {
            addHeader("a" to "b")
        }

        verifyHttpMockExecutedWithRequest(url = "url", headers = mapOf("a" to "b"))
    }

    fun `get Response4k`() {
        val expected = Response4k(SC_200_Ok, "body")
        wheneverExecuteHttpMockReturnResponse(expected)
        val http4k = http4kWithMock()

        val response: Response4k = Http4k2(http4k).get("url")

        response shouldMatchValue expected
    }

    fun `get String`() {
        val expected = Response4k(SC_200_Ok, "body")
        wheneverExecuteHttpMockReturnResponse(expected)
        val http4k = http4kWithMock()

        val response: String = Http4k2(http4k).get("url")

        response shouldMatchValue expected.bodyAsString
    }

    fun `get Jackson`() {
        val expected = Response4k(SC_200_Ok, dto.toJson())
        wheneverExecuteHttpMockReturnResponse(expected)
        val http4k = http4kWithMock()

        val response: Dto = Http4k2(http4k).get("url")

        response shouldMatchValue dto
    }

    fun `anyBodyfull with Dto`() {
        wheneverExecuteHttpMockReturnResponse()
        val http4k = http4kWithMock()

        Http4k2(http4k).anyBodyfull<Response4k>(HttpBodyfullMethod4k.POST, "url", dto)

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                url = "url",
                headers = mapOf("Content-Type" to "application/json"),
                requestBody = DefiniteRequestBody.DefiniteStringBody(dto.toJson()))
    }

    private data class Dto(val name: String, val age: Int) {
        companion object {
            val testee = Dto("a", 1)
        }
        fun toJson() = "{\"name\":\"$name\",\"age\":$age}"
    }

}
