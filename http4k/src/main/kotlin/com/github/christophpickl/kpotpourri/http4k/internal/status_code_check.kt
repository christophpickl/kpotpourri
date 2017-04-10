package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.Http4kStatusCodeException
import com.github.christophpickl.kpotpourri.http4k.Http4kStatusException
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.StatusCheckCustom
import com.github.christophpickl.kpotpourri.http4k.StatusCheckDisabled
import com.github.christophpickl.kpotpourri.http4k.StatusCheckEnfored
import com.github.christophpickl.kpotpourri.http4k.StatusCheckFail
import com.github.christophpickl.kpotpourri.http4k.StatusCheckOk


internal fun checkStatusCode(requestOpts: AnyRequestOpts, request4k: Request4k, response4k: Response4k) {
    // TODO support global statusCheck with lower precedence than request scoped

    val check = requestOpts.statusCheck
    when (check) {
        StatusCheckDisabled -> {
            return // do nothing :)
        }
        is StatusCheckEnfored -> if (response4k.statusCode != check.expectedStatusCode) {
            throw Http4kStatusCodeException(check.expectedStatusCode, response4k.statusCode)
        }
        is StatusCheckCustom -> {
            val result = check.checker(request4k, response4k)
            when (result) {
                StatusCheckOk -> {
                    return // succeeded
                }
                is StatusCheckFail -> {
                    throw Http4kStatusException(result.message)
                }
            }
        }
    }
}
