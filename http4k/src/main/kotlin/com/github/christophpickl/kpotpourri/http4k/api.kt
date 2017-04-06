package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.internal.Http4kImpl
import com.github.christophpickl.kpotpourri.http4k.internal.RestClientFactory
import kotlin.reflect.KClass


class Http4kBuilder : DefaultsOpts, DefaultsOptsReadOnly {

    override var baseUrl: BaseUrl = BaseUrl.NoBaseUrl

    fun withDefaults(wither: DefaultsOpts.() -> Unit): Http4kBuilder {
        wither.invoke(this)
        return this
    }

    fun end(): Http4k {
        val restClient = RestClientFactory.lookupRestClientByImplementation()
        return Http4kImpl(restClient, this)
    }
}

fun buildHttp4k(withBuilder: Http4kBuilder.() -> Unit): Http4k {
    val builder = Http4kBuilder()
    withBuilder.invoke(builder)
    return builder.end()
}


interface Http4k {

    fun get(url: String, withOpts: Http4kGetOpts.() -> Unit = {}): Response4k

    // MINOR could not add "returnType: KClass<R> = Response4k::class" ... :(
    fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: Http4kGetOpts.() -> Unit = {}): R

    fun post(url: String, withOpts: Http4kPostOpts.() -> Unit = {}): Response4k

    fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: Http4kPostOpts.() -> Unit = {}): R
}

data class Response4k(
        val statusCode: Int,
        val bodyAsString: String,
        val headers: Map<String, String>
        // header
        // cookies
)

// MINOR see com.github.tomakehurst.wiremock.http.RequestMethod
enum class HttpMethod4k(val isRequestBodySupported: Boolean = false) {
    GET(),
    POST(isRequestBodySupported = true)
    // PUT(isRequestBodySupported = true)
    // DELETE
    // PATCH(isRequestBodySupported = true)
    // OPTIONS
    // HEAD
    // TRACE
    // ANY
}
