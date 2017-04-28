package com.github.christophpickl.kpotpourri.web4k

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.christophpickl.kpotpourri.web4k.ErrorHandlerType.CustomHandler
import com.github.christophpickl.kpotpourri.web4k.ErrorHandlerType.DefaultHandler
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import org.eclipse.jetty.server.handler.ErrorHandler
import org.eclipse.jetty.util.ByteArrayISO8859Writer
import java.io.Writer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

sealed class ErrorHandlerType {
    object DefaultHandler : ErrorHandlerType()
    class CustomHandler(val handler: SpecificErrorHandler) : ErrorHandlerType()
}

interface SpecificErrorHandler {
    fun handle(error: ErrorHandlingObject)
}

data class ErrorHandlingObject(
        val request: HttpServletRequest,
        val response: HttpServletResponse,
        val writer: Writer,
        val exposeExceptions: Boolean
)


// TODO does not catch 404s
class RootErrorHandler(
        private val handlerType: ErrorHandlerType,
        private val exposeExceptions: Boolean
) : ErrorHandler() {
    private val log = com.github.christophpickl.kpotpourri.common.logging.LOG {}

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

        val handler: SpecificErrorHandler = when (handlerType) {
            DefaultHandler -> DefaultJsonErrorHandler
            is CustomHandler -> handlerType.handler
        }
        handler.handle(ErrorHandlingObject(
                request, response, writer, exposeExceptions
        ))

        writer.flush()
        response.setContentLength(writer.size())
        writer.writeTo(response.outputStream)
        writer.destroy()
    }

}



private object DefaultJsonErrorHandler : SpecificErrorHandler {

    private val mapper = jacksonObjectMapper() // TODO dont render NULLs

    override fun handle(error: ErrorHandlingObject) {
        error.response.contentType = "application/json"
        // maybe display request headers...?
        val stackTrace = if (error.exposeExceptions) {
            val thrown = error.request.getAttribute("javax.servlet.error.exception") as? Throwable
            thrown?.toStackTrace()
        } else {
            null
        }

        val json = mapper.writeValueAsString(JsonErrorPage(
                message = if (error.response is Response) error.response.reason else null,
                statusCode = error.response.status,
                // TODO if wrong media type is sent, this only contains "Bad Request",
                // but this is available: java.lang.IllegalArgumentException: RESTEASY003340: Failure parsing MediaType string: asdf
                errorMessage = error.request.getAttribute("javax.servlet.error.message") as String,
                requestMethod = error.request.method,
                requestUrl = error.request.requestURL.toString(),
                stackTrace = stackTrace
        ))
        error.writer.write(json)
    }

}

private data class StackTrace(
        /** The FQN. */
        val type: String,
        val message: String?,
        val trace: List<String>,
        val cause: StackTrace?
)

private fun Throwable.toStackTrace(): StackTrace {
    return StackTrace(javaClass.name, message, parseStackElements(stackTrace), cause?.toStackTrace())
}

private fun parseStackElements(elements: Array<StackTraceElement>): List<String> {
    return elements.map { "${it.className}#${it.methodName}() at ${it.fileName}:${it.lineNumber}" }
}

private data class JsonErrorPage(
        val requestMethod: String,
        val requestUrl: String,
        val statusCode: Int,
        /** The status code as a (human) readable string OR the concatenated exception messages. */
        val errorMessage: String,
        val message: String?,
        val stackTrace: StackTrace?
)
