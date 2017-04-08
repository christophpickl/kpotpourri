package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.http4k.internal.Http4kImpl
import com.github.christophpickl.kpotpourri.http4k.internal.RestClientFactory
import kotlin.reflect.KClass


class Http4kBuilder : DefaultsOpts {

    override var baseUrl: BaseUrl = NoBaseUrl
    override var basicAuth: BasicAuthMode = BasicAuthDisabled

//    fun withDefaults(wither: DefaultsOpts.() -> Unit): Http4kBuilder {
//        wither.invoke(this)
//        return this
//    }

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
    // MINOR could not add "returnType: KClass<R> = Response4k::class" ... :(

    fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: Http4kGetOpts.() -> Unit = {}): R

    fun get(url: String, withOpts: Http4kGetOpts.() -> Unit = {}): Response4k {
        return get(url, Response4k::class, withOpts)
    }

    fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: Http4kPostOpts.() -> Unit = {}): R

    fun post(url: String, withOpts: Http4kPostOpts.() -> Unit = {}): Response4k {
        return post(url, Response4k::class, withOpts)
    }

    // TODO test me
    fun <R : Any> post(url: String, jacksonObject: Any, returnType: KClass<R>, withOpts: Http4kPostOpts.() -> Unit = {}): R {
        return post(url, returnType, { bodyJson(jacksonObject); withOpts(this) })
    }
}


data class Request4k(
        val method: HttpMethod4k,
        val url: String,
        val requestBody: String? = null,
        val headers: Map<String, String> = emptyMap()
)

data class Response4k(
        val statusCode: StatusCode,
        val bodyAsString: String,
        val headers: Map<String, String>
        // header
        // cookies
)

// MINOR see com.github.tomakehurst.wiremock.http.RequestMethod
//   GET("GET"),
//POST("POST"),
//PUT("PUT"),
//DELETE("DELETE"),
//PATCH("PATCH"),
//HEAD("HEAD")
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

class Http4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)
