package com.github.christophpickl.kpotpourri.http4k

/**
 * Common settings for GET/POST/PUT/...
 */
interface AnyRequestOpts : StatusCheckConfig {
    val headers: MutableMap<String, String> // TODO make multi value map
    // queryParams
    // cookies
    var basicAuth: BasicAuthMode
}


/**
 * Got no body, opposed to POST/PUT requests.
 */
data class GetRequestOpts(
        override val headers: MutableMap<String, String> = HashMap(),
        override var basicAuth: BasicAuthMode = BasicAuthDisabled,
        override var statusCheck: StatusCheckMode = StatusCheckDisabled
) : AnyRequestOpts

/**
 * POST.
 */
data class PostRequestOpts(
        override val headers: MutableMap<String, String> = HashMap(),
        override var basicAuth: BasicAuthMode = BasicAuthDisabled,
        override var statusCheck: StatusCheckMode = StatusCheckDisabled,
        override var requestBody: RequestBody = None
) : AnyRequestOpts, RequestWithEntityOpts


// PUT
// DELETE
// ...
