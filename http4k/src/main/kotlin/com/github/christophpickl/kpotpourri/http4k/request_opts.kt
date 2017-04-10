package com.github.christophpickl.kpotpourri.http4k


interface StatusCheckConfig {

    var statusCheck: StatusCheckStrategy

    fun disableStatusCheck() {
        statusCheck = StatusCheckDisabled
    }

    fun enforceStatusCode(expectedStatusCode: StatusCode) {
        statusCheck = StatusCheckEnfored(expectedStatusCode)
    }

    fun customStatusCheck(checker: StatusCheckFunction) {
        statusCheck = StatusCheckCustom(checker)
    }

    fun enforceStatusFamily(family: StatusFamily) {
        statusCheck = StatusCheckCustom { _, response ->
            if (response.statusCode / 100 == family.group) {
                StatusCheckOk
            } else {
                StatusCheckFail("Status code ${response.statusCode} expected to be of group ${family.group}!")
            }
        }
    }
}

/**
 * Common settings for GET/POST/PUT/...
 */
interface Http4kAnyOpts: StatusCheckConfig {
    val headers: MutableMap<String, String> // TODO make multi value map
    // queryParams
    // cookies
    var basicAuth: BasicAuthMode
}

interface Http4kWithRequestEntity {
    var requestBody: RequestBody
}

/**
 * Got no body, opposed to POST/PUT requests.
 */
data class Http4kGetOpts(
        override val headers: MutableMap<String, String> = HashMap(),
        override var basicAuth: BasicAuthMode = BasicAuthDisabled,
        override var statusCheck: StatusCheckStrategy = StatusCheckDisabled
) : Http4kAnyOpts

/**
 * POST.
 */
data class Http4kPostOpts(
        override val headers: MutableMap<String, String> = HashMap(),
        override var basicAuth: BasicAuthMode = BasicAuthDisabled,
        override var statusCheck: StatusCheckStrategy = StatusCheckDisabled,
        override var requestBody: RequestBody = RequestBody.None
) : Http4kAnyOpts, Http4kWithRequestEntity

sealed class StatusCheckStrategy
object StatusCheckDisabled : StatusCheckStrategy()
class StatusCheckEnfored(val expectedStatusCode: StatusCode) : StatusCheckStrategy()

sealed class StatusCheckResult
object StatusCheckOk : StatusCheckResult()
data class StatusCheckFail(val message: String) : StatusCheckResult()

typealias StatusCheckFunction = (Request4k, Response4k) -> StatusCheckResult
class StatusCheckCustom(val checker: StatusCheckFunction) : StatusCheckStrategy()

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
