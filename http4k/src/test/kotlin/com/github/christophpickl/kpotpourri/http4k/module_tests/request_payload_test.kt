package com.github.christophpickl.kpotpourri.http4k.module_tests

import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.post
import com.github.christophpickl.kpotpourri.test4k.skip
import com.google.common.io.ByteSource
import org.testng.annotations.Test


@Test class RequestPayloadTest : ComponentTest() {

    private val requestBody = "requestBody"

    fun `When String request body, then string sent and plain text header set`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl, body = requestBody)

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "text/plain"),
                requestBody = DefiniteRequestBody.DefiniteStringBody(requestBody)
        )
    }

    fun `When Int request body, then Int sent and plain text header set`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl, body = 42)

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "text/plain"),
                requestBody = DefiniteRequestBody.DefiniteStringBody("42")
        )
    }

    fun `When Double request body, then Double sent and plain text header set`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl, body = 42.21)

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "text/plain"),
                requestBody = DefiniteRequestBody.DefiniteStringBody("42.21")
        )
    }

    fun `When Dto request body, then JSON sent and application json header set`() {
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl, body = Dto.testee)

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "application/json"),
                requestBody = DefiniteRequestBody.DefiniteStringBody(Dto.testee.toJson())
        )
    }

    fun `When ByteArray request body, then ByteArray sent`() {
        skip("not yet implemented. see: RequestWithEntityOpts.")
        val bytes = byteArrayOf(0, 1, 1, 0)
        wheneverExecuteHttpMockReturnResponse()

        http4kWithMock().post<Unit>(testUrl, body = bytes)

        verifyHttpMockExecutedWithRequest(
                method = HttpMethod4k.POST,
                headers = mapOf("Content-Type" to "???/???"),
                requestBody = DefiniteRequestBody.DefiniteBytesBody(ByteSource.wrap(bytes))
        )
    }


    private data class Dto(val name: String, val age: Int) {
        companion object {
            val testee = Dto("a", 1)
        }

        fun toJson() = "{\"name\":\"$name\",\"age\":$age}"
    }

}
