package com.github.christophpickl.kpotpourri.http4k


/**
 * Common settings for GET/POST/PUT/...
 */
interface Http4kAnyOpts {
    val headers: MutableMap<String, String> // TODO make multi value map
    // queryParams
    // cookies
    var basicAuth: BasicAuthMode
}

/**
 * Got no body, opposed to POST/PUT requests.
 */
data class Http4kGetOpts(
        override val headers: MutableMap<String, String> = HashMap(),
        override var basicAuth: BasicAuthMode = BasicAuthDisabled
) : Http4kAnyOpts

/**
 * POST.
 */
data class Http4kPostOpts (
        override val headers: MutableMap<String, String> = HashMap(),
        override var basicAuth: BasicAuthMode = BasicAuthDisabled,
        var requestBody: RequestBody = RequestBody.None
) : Http4kAnyOpts

sealed class BasicAuthMode
object BasicAuthDisabled : BasicAuthMode()
data class BasicAuth(
        val username: String,
        val password: String
) : BasicAuthMode()

// requestEntity
// ---------------------------------------------------------------------------------------------------------------------
// TODO test me
fun bodyDisabled() = RequestBody.None
fun bodyString(body: String) = RequestBody.StringBody(body)
fun bodyJson(jacksonObject: Any) = RequestBody.JsonBody(jacksonObject)

sealed class RequestBody {
    object None : RequestBody()
    class StringBody(val body: String) : RequestBody()
    class JsonBody(val jacksonObject: Any) : RequestBody()
}
