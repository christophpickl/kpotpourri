package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteBytesBody
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteStringBody
import com.github.christophpickl.kpotpourri.http4k.RequestBody.*
import com.github.christophpickl.kpotpourri.http4k.RequestWithEntityOpts
import com.github.christophpickl.kpotpourri.jackson4k.asString

private val log = LOG {}

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
