package com.github.christophpickl.kpotpourri.http4k


fun baseUrlBy(fullUrl: String) = BaseUrl.BaseUrlByString(fullUrl)
fun baseUrlBy(config: UrlConfig) = BaseUrl.BaseUrlByConfig(config)
fun baseUrlDisabled() = BaseUrl.NoBaseUrl

interface DefaultsOpts {
    var baseUrl: BaseUrl
}

interface DefaultsOptsReadOnly {
    val baseUrl: BaseUrl
}


sealed class BaseUrl {
    /**
     * Disable base URL feature.
     */
    object NoBaseUrl : BaseUrl() {
        override fun combine(url: String) = url
    }

    /**
     * @param fullUrl e.g.: "http://localhost:8080".
     */
    class BaseUrlByString(private val fullUrl: String) : BaseUrl() {
        override fun combine(url: String) = fullUrl + url
    }

    class BaseUrlByConfig(config: UrlConfig) : BaseUrl() {
        private val urlPrefix = "${config.protocol.urlPrefix}://${config.hostName}:${config.port}${config.path}"
        override fun combine(url: String) = urlPrefix + url
    }

    abstract fun combine(url: String): String
}

enum class HttpProtocol(val urlPrefix: String) {
    Http("http"), Https("https")
}

data class UrlConfig(
        // defaults to: "http://localhost:8080/"
        val protocol: HttpProtocol = HttpProtocol.Http,
        val hostName: String = "localhost",
        val port: Int = 8080,
        val path: String = "/"
)
