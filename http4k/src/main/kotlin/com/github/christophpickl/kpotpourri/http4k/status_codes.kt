@file:Suppress("unused")

package com.github.christophpickl.kpotpourri.http4k

@Suppress("CanBeParameter")
class Http4kStatusCodeException(
        val expected: StatusCode,
        val actual: StatusCode,
        cause: Exception? = null) :
        Http4kStatusException(buildMessage(expected, actual), cause) {

    companion object {
        private fun buildMessage(expected: StatusCode, actual: StatusCode) =
                "Got a $actual status code but expected $expected!"
    }
}

open class Http4kStatusException(
        message: String,
        cause: Exception? = null
) : Http4kException(message, cause)


enum class StatusFamily(val group: Int) {
    Info_1(1),
    Success_2(2),
    Redirection_3(3),
    ClientError_4(4),
    ServerError_5(5),
    Unofficial_6(6),
    See_7(7),
    Notes_8(8),
    References_9(9),
    External_10(10)
}

typealias StatusCode = Int

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
const val SC_405_NotFound = 405
const val SC_413_Payload = 413
const val SC_415_UnsupportedMime = 415
const val SC_418_Teapot = 418
const val SC_500_InternalError = 500
