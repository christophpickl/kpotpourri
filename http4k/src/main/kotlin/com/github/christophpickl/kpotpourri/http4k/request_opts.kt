package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.internal.HeadersMap

val DEFAULT_STATUS_CHECK_MODE = StatusCheckMode.NotSetAtAll
val DEFAULT_AUTH_MODE = BasicAuthDisabled


interface QueryParamConfig {
    val queryParams: MutableMap<String, String>

    fun addQueryParam(param: Pair<String, Any>) {
        queryParams += Pair(param.first, param.second.toString())
    }
}

/**
 * Common settings for GET/POST/PUT/...
 */
interface AnyRequestOpts : StatusCheckConfig, HeadersConfig, QueryParamConfig {
    override val queryParams: MutableMap<String, String>
    // queryParams
    // cookies
    var basicAuth: BasicAuthMode
}


/**
 * Got no body, opposed to POST/PUT requests.
 */
data class GetRequestOpts(
        override val headers: HeadersMap = HeadersMap(),
        override val queryParams: MutableMap<String, String> = HashMap(),
        override var statusCheck: StatusCheckMode = DEFAULT_STATUS_CHECK_MODE,
        override var basicAuth: BasicAuthMode = DEFAULT_AUTH_MODE
) : AnyRequestOpts

/**
 * POST.
 */
data class PostRequestOpts(
        override val headers: HeadersMap = HeadersMap(),
        override val queryParams: MutableMap<String, String> = HashMap(),
        override var statusCheck: StatusCheckMode = DEFAULT_STATUS_CHECK_MODE,
        override var basicAuth: BasicAuthMode = DEFAULT_AUTH_MODE,
        override var requestBody: RequestBody = None
) : AnyRequestOpts, RequestWithEntityOpts


// PUT
// DELETE
// ...
