package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.http4k.StatusCheckMode.Anything
import com.github.christophpickl.kpotpourri.http4k.internal.HeadersMap
import com.github.christophpickl.kpotpourri.http4k.internal.Http4kImpl
import com.github.christophpickl.kpotpourri.http4k.internal.RestClient
import com.github.christophpickl.kpotpourri.http4k.internal.RestClientFactory
import kotlin.reflect.KClass


fun buildHttp4k(withBuilder: Http4kBuilder.() -> Unit): Http4k {
    val builder = Http4kBuilder()
    withBuilder.invoke(builder)
    return builder.end()
}

class Http4kBuilder : GlobalHttp4kConfig {
    override val headers: HeadersMap = HeadersMap()
    override var baseUrl: BaseUrl = NoBaseUrl
    override var basicAuth: BasicAuthMode = BasicAuthDisabled
    override var statusCheck: StatusCheckMode = Anything

    internal var overrideRestClient: RestClient? = null

    fun end(): Http4k {
        val restClient = if(overrideRestClient != null) overrideRestClient!!
                else RestClientFactory.lookupRestClientByImplementation()
        return Http4kImpl(restClient, this)
    }
}

interface Http4k {

    fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: GetRequestOpts.() -> Unit = {}): R

    fun get(url: String, withOpts: GetRequestOpts.() -> Unit = {}): Response4k {
        return get(url, Response4k::class, withOpts)
    }

    fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: PostRequestOpts.() -> Unit = {}): R

    fun post(url: String, withOpts: PostRequestOpts.() -> Unit = {}): Response4k {
        return post(url, Response4k::class, withOpts)
    }

    fun <R : Any> post(url: String, jacksonObject: Any, returnType: KClass<R>, withOpts: PostRequestOpts.() -> Unit = {}): R {
        return post(url, returnType, { requestBody(jacksonObject); withOpts(this) })
    }
}


data class Request4k(
        val method: HttpMethod4k,
        val url: String,
        val headers: Map<String, String> = emptyMap(),
        // cookies
        val requestBody: String? = null
) {
    companion object {
        private fun filterAuthorizationHeader(headers: Map<String, String>): Map<String, String> {
            val newHeaders = HashMap<String, String>()
            headers.forEach { (k, v) ->
                if ("authorization" == k.toLowerCase()) {
                    newHeaders += k to "xxxxx"
                } else {
                    newHeaders += k to v
                }
            }
            return newHeaders
        }
    }

    override fun toString() = "Request4k(" +
            "method=$method, " +
            "url=$url, " +
            "headers=${filterAuthorizationHeader(headers)}" +
//            "cookies=$cookies, " +
            "requestBody=<<$requestBody>>, " +
            ")"
}

data class Response4k(
        val statusCode: StatusCode,
        val bodyAsString: String,
        val headers: Map<String, String> = emptyMap()
        // cookies
) {
    companion object // test extensions
}


open class Http4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)

interface GlobalHttp4kConfig :
        BaseUrlConfig,
        BasicAuthConfig,
        HeadersConfig,
        StatusCheckConfig
