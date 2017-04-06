package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Response4k


internal interface RestClient {
    fun execute(request: Request4k): Response4k
}

internal data class Request4k(
        val method: HttpMethod4k,
        val url: String,
        val headers: Map<String, String> = emptyMap()
        // TODO query param
        // TODO body (only when POST/PUT)
)
