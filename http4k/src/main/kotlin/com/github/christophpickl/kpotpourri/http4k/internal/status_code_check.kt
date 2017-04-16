package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.control.throwIf
import com.github.christophpickl.kpotpourri.common.enforceAllBranchesCovered
import com.github.christophpickl.kpotpourri.http4k.Http4kException
import com.github.christophpickl.kpotpourri.http4k.Http4kStatusCodeException
import com.github.christophpickl.kpotpourri.http4k.Http4kStatusException
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.StatusCheckMode
import com.github.christophpickl.kpotpourri.http4k.StatusCheckMode.*
import com.github.christophpickl.kpotpourri.http4k.StatusCheckResult.*
import com.github.christophpickl.kpotpourri.http4k.StatusRange.StatusByCode

internal fun requestScopeGoesBeforeGlobalScope(global: StatusCheckMode, request: StatusCheckMode): StatusCheckMode {
    if (global is NotSetAtAll && request is NotSetAtAll) {
        return NotSetAtAll
    }
    if (request !is NotSetAtAll) {
        return request
    }
    if (global !is NotSetAtAll) {
        return global
    }
    throw Http4kException("Impossible state ;) global=$global, request=$request")
}

/**
 * @throws Http4kStatusException if any check fails
 */
@Suppress("UNUSED_VARIABLE")
internal fun checkStatusCode(
        global: StatusCheckMode,
        request: StatusCheckMode,
        request4k: Request4k,
        response4k: Response4k
) {
    val check = requestScopeGoesBeforeGlobalScope(global, request)

    val checkForAllBranchesCovered = when (check) {
        NotSetAtAll -> {
            return // do nothing :)
        }
        Anything -> {
            return // do nothing :)
        }
        is Enfore -> {
            throwIf(response4k.statusCode != check.expectedStatusCode) {
                Http4kStatusCodeException(
                        expected = StatusByCode(check.expectedStatusCode),
                        actual = response4k.statusCode
                )
            }
        }
        is Custom -> {
            val result = check.checker(request4k, response4k)
            when (result) {
                Ok -> {
                    // succeeded
                }
                is FailWithException -> {
                    throw result.exceptionFunc(response4k)
                }
                is Fail -> {
                    throw Http4kStatusException(result.message)
                }
            }.enforceAllBranchesCovered
        }
    }
}
