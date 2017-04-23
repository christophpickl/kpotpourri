package com.github.christophpickl.kpotpourri.http4k_fuel

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody
import com.github.christophpickl.kpotpourri.http4k.Http4kException
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClient
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClientFactory
import com.github.christophpickl.kpotpourri.http4k.internal.MetaMap
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result

class FuelHttpClientFactory : HttpClientFactory {
    override fun build(metaMap: MetaMap) = FuelHttpClient(metaMap)
}

class FuelHttpClient(private val metaMap: MetaMap) : HttpClient {

    private val log = LOG {}

    init {
        // get rid of: redirectResponseInterceptor(this), validatorResponseInterceptor(200..299)
        FuelManager.instance.removeAllResponseInterceptors()
    }

    private fun String.httpAny(method: HttpMethod4k) =
        when (method) {
            HttpMethod4k.GET -> { httpGet() }
            HttpMethod4k.POST -> { httpPost() }
            HttpMethod4k.PUT -> { httpPut() }
            HttpMethod4k.DELETE -> { httpDelete() }
            HttpMethod4k.PATCH -> { httpPost() } // fuel hack, as it doesnt support patch
        }


    override fun execute(request4k: Request4k): Response4k {
        log.debug { "execute($request4k) ... $metaMap" }

        val (request, response, result) = request4k.url.httpAny(request4k.method)
                .apply {
                    header(request4k.headers)
                    if (request4k.method == HttpMethod4k.PATCH) {
                        header("X-HTTP-Method-Override" to "PATCH")
                    }
                    request4k.requestBody?.let {
                        when(it) {
                            is DefiniteRequestBody.DefiniteStringBody ->
                                body(it.string)
                            is DefiniteRequestBody.DefiniteBytesBody->
                                body(it.bytes.read())
                        }
                    }
                }
                // .timeout(timeout)
                // .readTimeout(readTimeout).
                .responseString()
                // .response(handler: (Request, Response, Result<ByteArray, FuelError>) -> Unit)

        val firstHeaderValuesOnly = response.httpResponseHeaders.map { it.key to it.value.first() }.toMap()
        return when (result) {
            is Result.Success -> Response4k(
                    statusCode = response.httpStatusCode,
                    bodyAsString = result.value,
                    headers = firstHeaderValuesOnly
            )
            is Result.Failure -> throw Http4kException("Failure result from fuel: $result") // if internal fuel error handlers got triggered
        }
    }

}
