package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Response4k


internal interface RestClient {
    fun execute(request: Request4k): Response4k
}

internal data class Request4k(
        val url: String,
        val method: HttpMethod4k,
        val headers: Map<String, String>
        // query param
        // body (only when POST/PUT)
)
