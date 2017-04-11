package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.JsonBody
import com.github.christophpickl.kpotpourri.http4k.None
import com.github.christophpickl.kpotpourri.http4k.RequestWithEntityOpts
import com.github.christophpickl.kpotpourri.http4k.StringBody


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
        is StringBody -> TypeAndBody("text/plain", body.stringEntity)
        is JsonBody -> TypeAndBody("application/json", mapper.writeValueAsString(body.jacksonEntity))
    }
}

internal data class TypeAndBody(
        val contentType: String,
        val requestBody: String
)
