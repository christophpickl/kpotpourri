package com.github.christophpickl.kpotpourri.web4k

import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.testng.annotations.Test
import java.io.StringWriter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Test class JsonErrorHandlerTest {

    fun `handle - sunshine test`() {
        val request = mock<HttpServletRequest> {
            on { method } doReturn "GET"
            on { requestURL } doReturn StringBuffer("url")
            on { getAttribute("javax.servlet.error.message") } doReturn "testErrorMessage"
        }
        val response = mock<HttpServletResponse> {
            on { status } doReturn SC_200_Ok
        }
        val writer = StringWriter()

        val errorObject = ErrorObject(request, response, writer, exposeExceptions = false)
        JsonErrorHandler.handle(errorObject)

        writer.toString() shouldMatchValue "{\"requestMethod\":\"GET\",\"requestUrl\":\"url\",\"statusCode\":200,\"errorMessage\":\"testErrorMessage\"}"
        verify(response).contentType = "application/json"
    }

}
