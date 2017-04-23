package com.github.christophpickl.kpotpourri.http4k_apache

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody
import com.github.christophpickl.kpotpourri.http4k.Http4kException
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClient
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClientFactory
import com.github.christophpickl.kpotpourri.http4k.internal.MetaMap
import com.github.christophpickl.kpotpourri.http4k.internal.TimeoutException
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPatch
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import java.io.ByteArrayOutputStream
import java.net.SocketTimeoutException


class ApacheHttpClientFactory : HttpClientFactory {
    override fun build(metaMap: MetaMap) =
            ApacheHttpClient(metaMap)
}

// https://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html
class ApacheHttpClient(private val metaMap: MetaMap) : HttpClient {

    private val log = LOG {}


    init {
        log.debug { "new ApacheHttpClientRestClient(metaMap=$metaMap)" }
    }

    override fun execute(request4k: Request4k): Response4k {
        val httpRequest = request4k.toHttpRequest()
        httpRequest.setHeaders(request4k.headers.entries.map { BasicHeader(it.key, it.value) }.toTypedArray())
        httpRequest.addBodyIfNecessary(request4k)

        httpRequest.config = RequestConfig.custom()
                .apply {
                    metaMap.requestTimeout?.let {
                        log.trace { "Setting request/socket timeout to: ${it}ms" }
                        setSocketTimeout(it)
//                        setConnectTimeout(it)
//                        setConnectionRequestTimeout(it)
                    }
                }
                .build()
        val client = HttpClientBuilder.create()
                .build()

        val response: CloseableHttpResponse?
        try {
            response = client.execute(httpRequest)
        } catch(e: SocketTimeoutException) {
            throw TimeoutException("Executing request failed because of a timeout! ($httpRequest)", e)
        }

        return response.toResponse4k()
    }

    private fun Request4k.toHttpRequest() = when (method) {
        HttpMethod4k.GET -> HttpGet(url)
        HttpMethod4k.POST -> HttpPost(url)
        HttpMethod4k.PATCH -> HttpPatch(url)
        HttpMethod4k.PUT -> HttpPut(url)
        HttpMethod4k.DELETE -> HttpDelete(url)
    }

    private fun HttpRequestBase.addBodyIfNecessary(request: Request4k) {
        if (!request.method.isRequestBodySupported) {
            return
        }
        if (this !is HttpEntityEnclosingRequestBase) {
            throw Http4kException("Expected HTTP request to be of type HttpEntityEnclosingRequestBase, but was: $this")
        }

        request.requestBody?.let { body ->
            val requestByteArray = when (body) {
            // or use StringEntity (better charset and content type support)
                is DefiniteRequestBody.DefiniteStringBody -> body.string.toByteArray()
                is DefiniteRequestBody.DefiniteBytesBody -> body.bytes.read()
            }
            this.entity = ByteArrayEntity(requestByteArray)
        }
    }

    private fun CloseableHttpResponse.toResponse4k() =
            Response4k(
                    statusCode = statusLine.statusCode,
                    bodyAsString = readBodyAsString(),
                    headers = allHeaders.map { it.name to it.value }.toMap()
            )

    private fun CloseableHttpResponse.readBodyAsString(): String {
        return ByteArrayOutputStream().use {
            this.entity.writeTo(it)
            String(it.toByteArray(), Charsets.UTF_8)
        }
    }

}
