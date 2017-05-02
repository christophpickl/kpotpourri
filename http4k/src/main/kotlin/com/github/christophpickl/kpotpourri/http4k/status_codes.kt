@file:Suppress("unused")

package com.github.christophpickl.kpotpourri.http4k


interface StatusCheckConfigurable {

    var statusCheck: StatusCheckMode

    fun unsetStatusCheck() {
        statusCheck = StatusCheckMode.NotSetAtAll
    }

    fun anyStatusCheck() {
        statusCheck = StatusCheckMode.Anything
    }

    fun enforceStatusCheck(expectedStatusCode: StatusCode) {
        statusCheck = StatusCheckMode.Enfore(expectedStatusCode)
    }

    fun customStatusCheck(checker: StatusCheckFunction) {
        statusCheck = StatusCheckMode.Custom(checker)
    }

    fun enforceStatusFamily(family: StatusFamily) {
        statusCheck = StatusCheckMode.EnforceFamily(family)
    }
}


sealed class StatusCheckMode {

    /**
     * Default behaviour if not set at all.
     * Used to indicate the request scope, when global scoped config is set and can take precedence.
     */
    object NotSetAtAll : StatusCheckMode()

    /**
     * All goes through.
     */
    object Anything : StatusCheckMode()

    /**
     * Enforce a specific HTTP status code Int.
     */
    class Enfore(val expectedStatusCode: StatusCode) : StatusCheckMode()

    /**
     * Take the request+response and return Ok or Fail (exception will be thrown).
     */
    open class Custom(val checker: StatusCheckFunction) : StatusCheckMode()

    class EnforceFamily(family: StatusFamily) : Custom({ _, (statusCode) ->
        if (family.matches(statusCode)) {
            StatusCheckResult.Ok
        } else {
            StatusCheckResult.FailWithException { _ ->
                Http4kStatusCodeException(
                        expected = StatusRange.StatusByFamily(family),
                        actual = statusCode)
            }
        }
    })

}

typealias StatusCheckFunction = (Request4k, Response4k) -> StatusCheckResult

sealed class StatusCheckResult {

    object Ok : StatusCheckResult()

    data class Fail(
            val message: String
    ) : StatusCheckResult()

    data class FailWithException(
            val exceptionFunc: (response4k: Response4k) -> Exception
    ) : StatusCheckResult()

}

sealed class StatusRange {
    data class StatusByCode(val code: StatusCode) : StatusRange() {
        override fun toPrettyString() = code.toString()
    }
    data class StatusByFamily(val family: StatusFamily) : StatusRange() {
        override fun toPrettyString() = family.prettyString
    }
    abstract fun toPrettyString(): String
}

@Suppress("CanBeParameter")
class Http4kStatusCodeException(
        val expected: StatusRange,
        val actual: StatusCode,
        additionalMessage: String? = null,
        cause: Exception? = null
) : Http4kStatusException(buildMessage(expected, actual, additionalMessage), cause) {

    companion object {
        private fun buildMessage(expected: StatusRange, actual: StatusCode, additionalMessage: String? = null) =
                "Got ${statusCode2Label(actual)} but expected ${expected.toPrettyString()}!" +
                        if (additionalMessage != null) " $additionalMessage" else ""
    }
}

open class Http4kStatusException(
        message: String,
        cause: Exception? = null
) : Http4kException(message, cause)


enum class StatusFamily(val group: Int, val label: String) {
    Info_1(1, "Info"),
    Success_2(2, "Success"),
    Redirection_3(3, "Redirection"),
    ClientError_4(4, "Client Error"),
    ServerError_5(5, "ServerError");

    val prettyString  get() = "${group}xx $label" // "2xx Success"
    fun matches(statusCode: StatusCode) =
            statusCode / 100 == group
}

typealias StatusCode = Int

fun statusCode2Label(search: StatusCode) =
    when (search) {
        200 -> "200 Ok"
        401 -> "401 Unauthorized"
        403 -> "403 Forbidden"
        else -> search.toString()
    }

const val SC_100_Continue = 100
const val SC_200_Ok = 200
const val SC_201_Created = 201
const val SC_202_Accepted = 202
const val SC_204_NoContent = 204
const val SC_301_Moved = 301
const val SC_302_Found = 302
const val SC_304_Unmodified = 304
const val SC_308_Redirect = 308
const val SC_400_BadRequest = 400
const val SC_401_Unauthorized = 401
const val SC_403_Forbidden = 403
const val SC_404_NotFound = 404
const val SC_413_Payload = 413
const val SC_415_UnsupportedMime = 415
const val SC_418_Teapot = 418
const val SC_500_InternalError = 500
const val SC_501_NotImplemented = 501
const val SC_503_ServiceUnavailable = 503
const val SC_504_GatewayTimeout = 504

val SCR_100_Continue = StatusRange.StatusByCode(SC_100_Continue)
val SCR_200_Ok = StatusRange.StatusByCode(SC_200_Ok)
val SCR_418_Teapot = StatusRange.StatusByCode(SC_418_Teapot)

val SR_1xx_Info = StatusRange.StatusByFamily(StatusFamily.Info_1)
val SR_2xx_Success = StatusRange.StatusByFamily(StatusFamily.Success_2)
val SR_3xx_Redirect = StatusRange.StatusByFamily(StatusFamily.Redirection_3)
val SR_4xx_ClientError = StatusRange.StatusByFamily(StatusFamily.ClientError_4)
val SR_5xx_ServerError = StatusRange.StatusByFamily(StatusFamily.ServerError_5)
