package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.internal.HeadersMap


interface HeadersConfig {
    val headers: HeadersMap

    // TODO add "accept", ... and other common stuff
    fun addHeader(header: Pair<String, String>) {
        headers += header
    }
}

enum class HttpProtocol(val urlPrefix: String) {
    Http("http"), Https("https")
}

data class UrlConfig(
        // defaults to: "http://localhost:80"
        val protocol: HttpProtocol = HttpProtocol.Http,
        val hostName: String = "localhost",
        val port: Int = 80,
        val path: String = "" // e.g.: "/rest"
)

//enum class HttpBodyfullMethod4k {
//    POST,
//    PUT,
//    PATCH
//}

// see com.github.tomakehurst.wiremock.http.RequestMethod
// GET("GET"),
// POST("POST"),
// PUT("PUT"),
// DELETE("DELETE"),
// PATCH("PATCH"),
// HEAD("HEAD")
enum class HttpMethod4k(val isRequestBodySupported: Boolean = false) {
    GET(),
    POST(isRequestBodySupported = true),
    PUT(isRequestBodySupported = true),
    DELETE(),
     PATCH(isRequestBodySupported = true)
    // OPTIONS(),
    // HEAD(),
    // TRACE(),
    // ANY()
}
