package com.github.christophpickl.kpotpourri.web4k

import com.github.christophpickl.kpotpourri.common.exception.formatted
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.jackson4k.JsonObject
import com.github.christophpickl.kpotpourri.jackson4k.asString
import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4k
import com.github.christophpickl.kpotpourri.web4k.ErrorHandlerType.Custom
import com.github.christophpickl.kpotpourri.web4k.ErrorHandlerType.Default
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import org.eclipse.jetty.server.handler.ErrorHandler
import org.eclipse.jetty.util.ByteArrayISO8859Writer
import java.io.Writer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Choose how to handle errors: A) default, 2) json or 3) custom defined handler.
 */
sealed class ErrorHandlerType {

    /** Means do nothing, let the default jetty handler do all the stuff (rendering HTML). */
    object Default : ErrorHandlerType()

    /** Respond in a JSON only style. */
    object Json : ErrorHandlerType()

    /** Define it the way you want :) */
    class Custom(val handler: CustomErrorHandler) : ErrorHandlerType()
}

/**
 * If ErrorHandlerType.Custom is set, this type is expected.
 */
interface CustomErrorHandler {
    fun handle(error: ErrorObject)
}

/**
 * Compound value object containing all relevant data to handle an error.
 */
data class ErrorObject(
        val request: HttpServletRequest,
        val response: HttpServletResponse,
        val writer: Writer,
        val exposeExceptions: Boolean
)


/**
 * Will be registered when setting up jetty.
 */
// TODO does not catch 404s
class RootErrorHandler(
        private val handlerType: ErrorHandlerType,
        private val exposeExceptions: Boolean
) : ErrorHandler() {

    private val log = LOG {}

    override fun handle(target: String?, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {
        log.debug { "handle(target=$target, baseRequest=$baseRequest, request=$request, response=$response)" }

        val handler: CustomErrorHandler = when (handlerType) {
            is Default -> return super.handle(target, baseRequest, request, response)
            ErrorHandlerType.Json -> JsonErrorHandler
            is Custom -> handlerType.handler
        }

        baseRequest.isHandled = true
        response.setHeader("Cache-Control", "must-revalidate,no-cache,no-store")
        ByteArrayISO8859Writer(4096).writeToResponse(response) { writer ->
            handler.handle(ErrorObject(request, response, writer, exposeExceptions))
        }
    }

    fun ByteArrayISO8859Writer.writeToResponse(response: HttpServletResponse, func: (ByteArrayISO8859Writer) -> Unit) {
        func(this)
        flush()
        response.setContentLength(size())
        writeTo(response.outputStream)
        destroy()
    }

}

/**
 * Renders proper JSON.
 */
private object JsonErrorHandler : CustomErrorHandler {

    private val SERVLET_ATTRIBUTE_EXCEPTION = "javax.servlet.error.exception"
    private val SERVLET_ATTRIBUTE_ERROR_MESSAGE = "javax.servlet.error.message"

    private val mapper = buildJackson4k() // TODO dont render NULLs => use jackson4k (easier configuration)

    override fun handle(error: ErrorObject) {
        error.response.contentType = "application/json"
        // maybe display request headers...?

        val json = mapper.asString(ErrorResponse(
                message = if (error.response is Response) error.response.reason else null,
                statusCode = error.response.status,
                // TODO if wrong media type is sent, this only contains "Bad Request",
                // but this is available: java.lang.IllegalArgumentException: RESTEASY003340: Failure parsing MediaType string: asdf
                errorMessage = error.request.getAttribute(SERVLET_ATTRIBUTE_ERROR_MESSAGE) as String,
                requestMethod = error.request.method,
                requestUrl = error.request.requestURL.toString(),
                stackTrace = buildStackTrace(error)
        ))
        error.writer.write(json)
    }

    private fun buildStackTrace(error: ErrorObject): StackTrace? {
        if (!error.exposeExceptions) {
            return null
        }
        val thrown = error.request.getAttribute(SERVLET_ATTRIBUTE_EXCEPTION) as? Throwable
        return thrown?.toStackTrace()
    }

}

private fun Throwable.toStackTrace(): StackTrace =
        StackTrace(
                type = javaClass.name,
                message = message,
                trace = stackTrace.formatted(),
                cause = cause?.toStackTrace()
        )

@JsonObject
data class ErrorResponse(
        val requestMethod: String,
        val requestUrl: String,
        val statusCode: Int,
        /** The status code as a (human) readable string OR the concatenated exception messages. */
        val errorMessage: String,
        val message: String?,
        val stackTrace: StackTrace?
)

@JsonObject
data class StackTrace(
        /** The full qualified name of the exception class. */
        val type: String,

        /** The (optional) exception message. */
        val message: String?,

        /** Stack trace elements formatted as string. */
        val trace: List<String>,

        /** Recursive root cause until we reach the end. */
        val cause: StackTrace?
)
