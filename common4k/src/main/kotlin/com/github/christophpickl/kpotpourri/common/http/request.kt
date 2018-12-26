package com.github.christophpickl.kpotpourri.common.http

import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

inline fun <T> httpRequest(url: String, method: String, action: HttpRequestor.() -> T): T {
    return action(HttpRequestor(url, method))
}

class HttpRequestor(
    private val url: String,
    private val method: String
) {
    private val headers = mutableMapOf<String, String>()
    private var withOutput: ((OutputStream) -> Unit)? = null
    var readResponseBody = false

    fun addHeader(header: Pair<String, String>) {
        headers += header
    }

    fun doWithOutput(action: (OutputStream) -> Unit) {
        withOutput = action
    }

    fun execute(): HttpResponse {
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = method
            headers.forEach { key, value ->
                connection.setRequestProperty(key, value)
            }
            withOutput?.let {
                connection.doOutput = true
                it(connection.outputStream)
            }
            return HttpResponse(
                statusCode = connection.responseCode,
                responseBody = if (readResponseBody) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else null
            )
        } finally {
            connection.disconnect()
        }
    }
}

class HttpResponse(
    val statusCode: Int,
    val responseBody: String?
)
