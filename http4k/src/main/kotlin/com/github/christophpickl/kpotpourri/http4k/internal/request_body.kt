package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteBytesBody
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteStringBody
import com.github.christophpickl.kpotpourri.http4k.RequestBody.*
import com.github.christophpickl.kpotpourri.http4k.RequestWithEntityOpts


/**
 * @return Pair of the content type and the actual response body content
 */
internal fun prepareBodyAndContentType(requestOpts: AnyRequestOpts): TypeAndBody? {
    if (requestOpts !is RequestWithEntityOpts) {
        return null
    }

    val body = requestOpts.requestBody
    return when (body) {
        is None -> null
        is StringBody -> TypeAndBody("text/plain", DefiniteStringBody(body.stringEntity))
        is JsonBody -> TypeAndBody("application/json", DefiniteStringBody(mapper.writeValueAsString(body.jacksonEntity)))
        is BytesBody -> TypeAndBody(body.contentType, DefiniteBytesBody(body.bytes))
    }
}

internal data class TypeAndBody(
        val contentType: String,
        val requestBody: DefiniteRequestBody
)
