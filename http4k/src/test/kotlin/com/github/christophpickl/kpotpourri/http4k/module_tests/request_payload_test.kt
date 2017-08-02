package com.github.christophpickl.kpotpourri.http4k.module_tests

import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.post
import com.github.christophpickl.kpotpourri.jackson4k.asString
import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4kMapper
import org.testng.annotations.Test


@Test class RequestPayloadTest : ComponentTest() {

    companion object {
        private val mapper = buildJackson4kMapper()
    }

    private val anyRequestBody = "anyRequestBody"

    fun `When String request body, then string sent and plain text header set`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl) {
            requestBody(anyRequestBody)
        }

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "text/plain"),
                requestBody = DefiniteRequestBody.DefiniteStringBody(anyRequestBody)
        )
    }

    fun `When Int request body, then Int sent and plain text header set`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl) {
            requestBody(42)
        }

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "text/plain"),
                requestBody = DefiniteRequestBody.DefiniteStringBody("42")
        )
    }

    fun `When Double request body, then Double sent and plain text header set`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl) {
            requestBody(42.21)
        }

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "text/plain"),
                requestBody = DefiniteRequestBody.DefiniteStringBody("42.21")
        )
    }

    fun `When Dto request body, then JSON sent and application json header set`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl) {
            requestBody(Dto.testee)
        }

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "application/json"),
                requestBody = DefiniteRequestBody.DefiniteStringBody(Dto.testee.toJson())
        )
    }

    fun `When ByteArray request body, then ByteArray sent`() {
        val bytes = byteArrayOf(0, 1, 1, 0)
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl) {
            requestBody(bytes)
        }

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "*/*"),
                requestBody = DefiniteRequestBody.DefiniteBytesBody(bytes)
        )
    }


    private data class Dto(val name: String, val age: Int) {
        companion object {
            val testee = Dto("a", 1)
        }

        fun toJson() = mapper.asString(this)
    }

}
