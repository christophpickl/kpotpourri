package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.BasicAuthMode.BasicAuthDisabled
import com.github.christophpickl.kpotpourri.http4k.RequestBody.None
import com.github.christophpickl.kpotpourri.http4k.internal.HeadersMap

val DEFAULT_STATUS_CHECK_MODE = StatusCheckMode.NotSetAtAll
val DEFAULT_AUTH_MODE = BasicAuthDisabled


interface QueryParamConfigurable {
    val queryParams: MutableMap<String, String>

    fun addQueryParam(param: Pair<String, Any>) {
        queryParams += Pair(param.first, param.second.toString())
    }
}

/**
 * Common settings for GET/POST/PUT/...
 */
interface AnyRequestOpts : StatusCheckConfigurable, HeadersConfigurable, QueryParamConfigurable {
    override val queryParams: MutableMap<String, String>
    // queryParams
    // cookies
    var basicAuth: BasicAuthMode
    var disableBaseUrl: Boolean
    fun disableBaseUrl() { disableBaseUrl = true }
}


/**
 * Got no body, opposed to POST/PUT/etc requests.
 */
data class BodylessRequestOpts(
        override val headers: HeadersMap = HeadersMap(),
        override val queryParams: MutableMap<String, String> = HashMap(),
        override var statusCheck: StatusCheckMode = DEFAULT_STATUS_CHECK_MODE,
        override var basicAuth: BasicAuthMode = DEFAULT_AUTH_MODE,
        override var disableBaseUrl: Boolean = false
) : AnyRequestOpts

/**
 * Got additional requestBody parameter, for POST/PUT/etc.
 */
data class BodyfullRequestOpts(
        override val headers: HeadersMap = HeadersMap(),
        override val queryParams: MutableMap<String, String> = HashMap(),
        override var statusCheck: StatusCheckMode = DEFAULT_STATUS_CHECK_MODE,
        override var basicAuth: BasicAuthMode = DEFAULT_AUTH_MODE,
        override var disableBaseUrl: Boolean = false,

        override var requestBody: RequestBody = None
) : AnyRequestOpts, RequestWithEntityOpts
