package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteBytesBody
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteStringBody
import com.github.christophpickl.kpotpourri.http4k.RequestBody.BytesBody
import com.github.christophpickl.kpotpourri.http4k.RequestBody.JsonBody
import com.github.christophpickl.kpotpourri.http4k.RequestBody.None
import com.github.christophpickl.kpotpourri.http4k.RequestBody.StringBody
import com.github.christophpickl.kpotpourri.http4k.RequestWithEntityOpts
import com.github.christophpickl.kpotpourri.jackson4k.asString
import mu.KotlinLogging.logger

private val log = logger {}

/**
 * @return Pair of the content type and the actual response body content
 */
internal fun prepareBodyAndContentType(requestOpts: AnyRequestOpts): TypeAndBody? {
    if (requestOpts !is RequestWithEntityOpts) {
        log.trace("Not preparing body as request is not of type RequestWithEntityOpts.")
        return null
    }

    val body = requestOpts.requestBody
    return when (body) {
        is None -> null
        is StringBody -> TypeAndBody(body.contentType, DefiniteStringBody(body.stringEntity))
        is JsonBody -> TypeAndBody(body.contentType, DefiniteStringBody(mapper.asString(body.jacksonEntity)))
        is BytesBody -> TypeAndBody(body.contentType, DefiniteBytesBody(body.bytes))
    }
}

internal data class TypeAndBody(
        val contentType: String,
        val requestBody: DefiniteRequestBody
)
