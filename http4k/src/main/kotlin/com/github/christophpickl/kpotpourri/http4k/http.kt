package com.github.christophpickl.kpotpourri.http4k

/**
 * Supported protocols as part of an URL.
 */
enum class HttpProtocol(val urlPrefix: String) {
    /** Unsecure. */
    Http("http"),
    /** Secure. */
    Https("https")
}

/**
 * Common interface defining the components of an URL.
 */
interface UrlConfig {

    /** HTTP or HTTPS. */
    val protocol: HttpProtocol

    /** E.g.: "localhost" */
    val hostName: String

    /** E.g.: 80 or 443 */
    val port: Int

}

/**
 * Abstraction of a full URL split into its separate components including a path prefix.
 */
data class BaseUrlConfig(
        // defaults to: "http://localhost:80"
        override val protocol: HttpProtocol = HttpProtocol.Http,
        override val hostName: String = "localhost",
        override val port: Int = 80,
        /** Useful to map servlet filters, e.g.: "/rest" */
        val path: String = ""
) : UrlConfig

/**
 * Abstraction of a full URL split into its separate components.
 */
data class ServerConfig(
        override val protocol: HttpProtocol,
        override val hostName: String,
        override val port: Int
) : UrlConfig

/**
 * Converts the one to the other by taking the missing path.
 */
fun ServerConfig.toBaseUrlConfig(path: String) = BaseUrlConfig(
        protocol = protocol,
        hostName = hostName,
        port = port,
        path = path
)
