package com.github.christophpickl.kpotpourri.http4k.internal.implementations

import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.internal.Request4k
import com.github.christophpickl.kpotpourri.http4k.internal.RestClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import java.io.ByteArrayOutputStream

// https://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html
internal class ApacheHttpClientRestClient : RestClient {

    override fun execute(request: Request4k): Response4k {
        val client = HttpClients.createDefault()
        val get = HttpGet(request.url)
        get.setHeaders(request.headers.entries.map { BasicHeader(it.key, it.value) }.toTypedArray())

        val response = client.execute(get)

        // MINOR lazily store bodyAsString
        val outStream = ByteArrayOutputStream()
        response.entity.writeTo(outStream)
        val bodyAsString = String(outStream.toByteArray(), Charsets.UTF_8)

        val headers = response.allHeaders.map { it.name to it.value }.toMap()

        return Response4k(response.statusLine.statusCode, bodyAsString, headers)
    }

}
