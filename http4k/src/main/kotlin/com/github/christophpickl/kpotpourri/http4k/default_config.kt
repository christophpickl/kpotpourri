package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.string.concatUrlParts


interface DefaultsOpts {
    var baseUrl: BaseUrl

    fun baseUrlDisabled() {
        baseUrl = NoBaseUrl
    }

    fun baseUrlBy(fullUrl: String) {
        baseUrl = BaseUrlByString(fullUrl)
    }

    fun baseUrlBy(config: UrlConfig) {
        baseUrl = BaseUrlByConfig(config)
    }

    var basicAuth: BasicAuthMode

    fun basicAuth(username: String, password: String) {
        basicAuth = BasicAuth(username, password)
    }
}

sealed class BaseUrl {
    abstract fun combine(url: String): String
}

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
    override fun combine(url: String) = concatUrlParts(fullUrl, url)
}

class BaseUrlByConfig(config: UrlConfig) : BaseUrl() {
    private val urlPrefix =
            concatUrlParts("${config.protocol.urlPrefix}://${config.hostName}:${config.port}", config.path)

    override fun combine(url: String) = concatUrlParts(urlPrefix, url)
}

enum class HttpProtocol(val urlPrefix: String) {
    Http("http"), Https("https")
}

data class UrlConfig(
        // defaults to: "http://localhost:8080"
        val protocol: HttpProtocol = HttpProtocol.Http,
        val hostName: String = "localhost",
        val port: Int = 80,
        val path: String = "" // e.g.: "/rest"
)
