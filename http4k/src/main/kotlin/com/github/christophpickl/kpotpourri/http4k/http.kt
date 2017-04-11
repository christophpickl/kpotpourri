package com.github.christophpickl.kpotpourri.http4k


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


// see com.github.tomakehurst.wiremock.http.RequestMethod
// GET("GET"),
// POST("POST"),
// PUT("PUT"),
// DELETE("DELETE"),
// PATCH("PATCH"),
// HEAD("HEAD")
enum class HttpMethod4k(val isRequestBodySupported: Boolean = false) {
    GET(),
    POST(isRequestBodySupported = true)
    // PUT(isRequestBodySupported = true)
    // DELETE(),
    // PATCH(isRequestBodySupported = true)
    // OPTIONS(),
    // HEAD(),
    // TRACE(),
    // ANY()
}
