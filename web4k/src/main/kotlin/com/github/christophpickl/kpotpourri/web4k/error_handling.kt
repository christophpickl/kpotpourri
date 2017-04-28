package com.github.christophpickl.kpotpourri.web4k

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import org.eclipse.jetty.server.handler.ErrorHandler
import org.eclipse.jetty.util.ByteArrayISO8859Writer
import java.io.Writer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


data class StackTrace(
        /** The FQN. */
        val type: String,
        val message: String?,
        val trace: List<String>,
        val cause: StackTrace?
)

fun Throwable.toStackTrace(): StackTrace {
    return StackTrace(javaClass.name, message, parseStackElements(stackTrace), cause?.toStackTrace())
}

private fun parseStackElements(elements: Array<StackTraceElement>): List<String> {
    return elements.map { "${it.className}#${it.methodName}() at ${it.fileName}:${it.lineNumber}" }
}

data class JsonErrorPage(
        val requestMethod: String,
        val requestUrl: String,
        val statusCode: Int,
        /** The status code as a (human) readable string OR the concatenated exception messages. */
        val errorMessage: String,
        val message: String?,
        val stackTrace: StackTrace?
)

// TODO does not catch 404s
class JsonErrorHandler(private val exposeExceptions: Boolean) : ErrorHandler() {
    private val log = com.github.christophpickl.kpotpourri.common.logging.LOG {}
    private val mapper = jacksonObjectMapper() // TODO dont render NULLs

    override fun handle(target: String?, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {
        log.debug { "handle(target=$target, baseRequest=$baseRequest, request=$request, response=$response)" }
//        val method = request.method
//    if (!HttpMethod.GET.`is`(method) && !HttpMethod.POST.`is`(method) && !HttpMethod.HEAD.`is`(method)) {
//        baseRequest.isHandled = true
//        return
//    }

        baseRequest.isHandled = true
//    if (_cacheControl != null)
//        response.setHeader(HttpHeader.CACHE_CONTROL.asString(), _cacheControl)
        val writer = ByteArrayISO8859Writer(4096)

        handleJsonErrorPage(request, response, writer)
        writer.flush()
        response.setContentLength(writer.size())
        writer.writeTo(response.outputStream)
        writer.destroy()
    }

    private fun handleJsonErrorPage(request: HttpServletRequest, response: HttpServletResponse, writer: Writer) {
        response.contentType = "application/json"
        // maybe display request headers...?
        val stackTrace = if (exposeExceptions) {
            val thrown = request.getAttribute("javax.servlet.error.exception") as? Throwable
            thrown?.toStackTrace()
        } else {
            null
        }

        val json = mapper.writeValueAsString(JsonErrorPage(
                message = if (response is Response) response.reason else null,
                statusCode = response.status,
                // TODO if wrong media type is sent, this only contains "Bad Request",
                // but this is available: java.lang.IllegalArgumentException: RESTEASY003340: Failure parsing MediaType string: asdf
                errorMessage = request.getAttribute("javax.servlet.error.message") as String,
                requestMethod = request.method,
                requestUrl = request.requestURL.toString(),
                stackTrace = stackTrace
        ))
        writer.write(json)
    }

}
