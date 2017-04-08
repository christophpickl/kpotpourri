package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k


internal interface RestClient {
    fun execute(request: Request4k): Response4k
}
