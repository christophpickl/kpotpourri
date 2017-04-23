package com.github.christophpickl.kpotpourri.http4k_fuel

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClient
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClientFactory
import com.github.christophpickl.kpotpourri.http4k.internal.MetaMap

class FuelHttpClientFactory : HttpClientFactory {
    override fun build(metaMap: MetaMap) = FuelHttpClient(metaMap)
}

class FuelHttpClient(private val metaMap: MetaMap) : HttpClient {

    private val log = LOG {}

    override fun execute(request: Request4k): Response4k {
        log.debug { "execute($request) ... $metaMap" }

        // headers = listOf("X-HTTP-Method-Override" to "PATCH"), // HttpURLConnection hack which does not support PATCH method

        return Response4k(
                statusCode = SC_200_Ok,
                bodyAsString = "",
                headers = emptyMap()
        )
    }

}
