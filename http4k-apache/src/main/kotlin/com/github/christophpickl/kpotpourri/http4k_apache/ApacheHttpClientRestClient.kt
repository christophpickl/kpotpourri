package com.github.christophpickl.kpotpourri.http4k_apache

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.Http4kException
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.internal.RestClient
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import java.io.ByteArrayOutputStream


// https://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html
class ApacheHttpClientRestClient : RestClient {

    private val log = LOG {}

    init {
        log.debug("new ApacheHttpClientRestClient()")
    }

    override fun execute(request: Request4k): Response4k {
        val httpRequest = request.toHttpRequest()
        httpRequest.setHeaders(request.headers.entries.map { BasicHeader(it.key, it.value) }.toTypedArray())
        httpRequest.addBodyIfNecessary(request)

        val client = HttpClients.createDefault()
//        httpRequest.config.connectTimeout
//        httpRequest.config.connectionRequestTimeout
//        httpRequest.config.socketTimeout

        val response = client.execute(httpRequest)

        return response.toResponse4k()
    }

    private fun Request4k.toHttpRequest() = when (method) {
        HttpMethod4k.GET -> HttpGet(url)
        HttpMethod4k.POST -> HttpPost(url)
    }

    private fun HttpRequestBase.addBodyIfNecessary(request: Request4k) {
        if (!request.method.isRequestBodySupported && request.requestBody != null) {
            throw Http4kException("Invalid request! HTTP method [${request.method}] does not support request body: ${request.requestBody}")
        }

        if (request.method.isRequestBodySupported) {
            if (this !is HttpEntityEnclosingRequestBase) {
                throw Http4kException("Expected HTTP request to be of type HttpEntityEnclosingRequestBase, but was: $this")
            }

            request.requestBody?.let { body ->
                this.entity = ByteArrayEntity(body.toByteArray())
            }
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