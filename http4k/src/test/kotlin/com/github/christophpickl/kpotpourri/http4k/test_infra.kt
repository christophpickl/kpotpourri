package com.github.christophpickl.kpotpourri.http4k

val Request4k.Companion.testDummy: Request4k get() = Request4k(
        method = HttpMethod4k.GET,
        url = "/my"
)

val Response4k.Companion.testDummy: Response4k get() = Response4k(
        statusCode = SC_200_Ok,
        bodyAsString = "response body"
)
