package com.github.christophpickl.kpotpourri.http4k

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.ByteArrayOutputStream


// https://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html
internal class ApacheHttpClientRestClient : RestClient {

    override fun execute(request: Request): Response {
        val client = HttpClients.createDefault()
        val get = HttpGet(request.url)
        val response = client.execute(get)
        val outStream = ByteArrayOutputStream()
        response.entity.writeTo(outStream)
        val bodyAsString = String(outStream.toByteArray(), Charsets.UTF_8)
        return Response(response.statusLine.statusCode, bodyAsString)
    }

}
