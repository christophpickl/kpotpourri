package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.RequestBody.None
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
    var disableBaseUrl: Boolean
    fun disableBaseUrl() { disableBaseUrl = true }
}


/**
 * Got no body, opposed to POST/PUT requests.
 */
data class GetRequestOpts(
        override val headers: HeadersMap = HeadersMap(),
        override val queryParams: MutableMap<String, String> = HashMap(),
        override var statusCheck: StatusCheckMode = DEFAULT_STATUS_CHECK_MODE,
        override var basicAuth: BasicAuthMode = DEFAULT_AUTH_MODE,
        override var disableBaseUrl: Boolean = false
) : AnyRequestOpts

/**
 * POST.
 */
data class PostRequestOpts(
        override val headers: HeadersMap = HeadersMap(),
        override val queryParams: MutableMap<String, String> = HashMap(),
        override var statusCheck: StatusCheckMode = DEFAULT_STATUS_CHECK_MODE,
        override var basicAuth: BasicAuthMode = DEFAULT_AUTH_MODE,
        override var disableBaseUrl: Boolean = false,

        override var requestBody: RequestBody = None
) : AnyRequestOpts, RequestWithEntityOpts

/**
 * PUT.
 */
data class PutRequestOpts(
        override val headers: HeadersMap = HeadersMap(),
        override val queryParams: MutableMap<String, String> = HashMap(),
        override var statusCheck: StatusCheckMode = DEFAULT_STATUS_CHECK_MODE,
        override var basicAuth: BasicAuthMode = DEFAULT_AUTH_MODE,
        override var disableBaseUrl: Boolean = false,

        override var requestBody: RequestBody = None
) : AnyRequestOpts, RequestWithEntityOpts

/**
 * DELETE.
 */
data class DeleteRequestOpts(
        override val headers: HeadersMap = HeadersMap(),
        override val queryParams: MutableMap<String, String> = HashMap(),
        override var statusCheck: StatusCheckMode = DEFAULT_STATUS_CHECK_MODE,
        override var basicAuth: BasicAuthMode = DEFAULT_AUTH_MODE,
        override var disableBaseUrl: Boolean = false,

        override var requestBody: RequestBody = None
) : AnyRequestOpts, RequestWithEntityOpts

/**
 * PATCH.
 */
data class PatchRequestOpts(
        override val headers: HeadersMap = HeadersMap(),
        override val queryParams: MutableMap<String, String> = HashMap(),
        override var statusCheck: StatusCheckMode = DEFAULT_STATUS_CHECK_MODE,
        override var basicAuth: BasicAuthMode = DEFAULT_AUTH_MODE,
        override var disableBaseUrl: Boolean = false,

        override var requestBody: RequestBody = None
) : AnyRequestOpts, RequestWithEntityOpts
