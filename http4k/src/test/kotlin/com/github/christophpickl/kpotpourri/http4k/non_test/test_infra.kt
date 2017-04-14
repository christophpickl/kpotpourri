package com.github.christophpickl.kpotpourri.http4k.non_test

import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok

val Request4k.Companion.testDummy: Request4k get() = Request4k(
        method = HttpMethod4k.GET,
        url = "/my"
)

val Response4k.Companion.testDummy: Response4k get() = Response4k(
        statusCode = SC_200_Ok,
        bodyAsString = "response body"
)
