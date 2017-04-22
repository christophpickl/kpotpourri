package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.http4k.StatusCheckMode.Anything
import com.github.christophpickl.kpotpourri.http4k.internal.HeadersMap
import com.github.christophpickl.kpotpourri.http4k.internal.Http4kImpl
import com.github.christophpickl.kpotpourri.http4k.internal.HttpImpl
import com.github.christophpickl.kpotpourri.http4k.internal.MutableMetaMap
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

    internal var overrideHttpImpl: HttpImpl? = null
    val _implMetaMap = MutableMetaMap()

    fun end(): Http4k {
        val restClient = if (overrideHttpImpl != null) overrideHttpImpl!!
        else RestClientFactory.lookupRestClientByImplementation().build(_implMetaMap)
        return Http4kImpl(restClient, this)
    }
}

class Http4k2(val http4k: Http4k) {

    inline fun <reified R : Any> anyBodyfull(method: HttpBodyfullMethod4k, url: String, body: Any? = null, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) {
        when (method) {
            HttpBodyfullMethod4k.POST -> if (body == null) http4k.post(url, R::class, withOpts) else http4k.post(url, body, R::class, withOpts)
            HttpBodyfullMethod4k.PUT -> if (body == null) http4k.put(url, R::class, withOpts) else http4k.put(url, body, R::class, withOpts)
            HttpBodyfullMethod4k.PATCH -> if (body == null) http4k.patch(url, R::class, withOpts) else http4k.patch(url, body, R::class, withOpts)
        }.let { }
    }

    inline fun <reified R : Any> get(url: String, noinline withOpts: BodylessRequestOpts.() -> Unit = {}) = http4k.get(url, R::class, withOpts)
    inline fun <reified R : Any> post(url: String, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) = http4k.post(url, R::class, withOpts)
    inline fun <reified R : Any> put(url: String, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) = http4k.put(url, R::class, withOpts)
    inline fun <reified R : Any> delete(url: String, noinline withOpts: BodylessRequestOpts.() -> Unit = {}) = http4k.delete(url, R::class, withOpts)
    inline fun <reified R : Any> patch(url: String, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) = http4k.patch(url, R::class, withOpts)

}

/**
 * Core interface to execute HTTP requests for any method (GET, POST, ...) configurable via request options.
 *
 * return type: either any jackson unmarshallable object, or an [Response4k] instance by default.
 * url: combined with optional global base URL
 * body: something which will be marshalled by jackson
 */
interface Http4k {

    fun get(url: String, withOpts: BodylessRequestOpts.() -> Unit = {}) = get(url, Response4k::class, withOpts)
    fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit = {}): R

    fun post(url: String, withOpts: BodyfullRequestOpts.() -> Unit = {}) = post(url, Response4k::class, withOpts)
    fun <R : Any> post(url: String, body: Any, withOpts: BodyfullRequestOpts.() -> Unit = {}) = post(url, Response4k::class, { requestBody(body); withOpts(this) })
    fun <R : Any> post(url: String, body: Any, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}) = post(url, returnType, { requestBody(body); withOpts(this) })
    fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

    fun put(url: String, withOpts: BodyfullRequestOpts.() -> Unit = {}) = put(url, Response4k::class, withOpts)
    fun <R : Any> put(url: String, body: Any, withOpts: BodyfullRequestOpts.() -> Unit = {}) = put(url, Response4k::class, { requestBody(body); withOpts(this) })
    fun <R : Any> put(url: String, body: Any, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}) = put(url, returnType, { requestBody(body); withOpts(this) })
    fun <R : Any> put(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

    fun delete(url: String, withOpts: BodylessRequestOpts.() -> Unit = {}) = delete(url, Response4k::class, withOpts)
    fun <R : Any> delete(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit = {}): R

    fun patch(url: String, withOpts: BodyfullRequestOpts.() -> Unit = {}) = patch(url, Response4k::class, withOpts)
    fun <R : Any> patch(url: String, body: Any, withOpts: BodyfullRequestOpts.() -> Unit = {}) = patch(url, Response4k::class, { requestBody(body); withOpts(this) })
    fun <R : Any> patch(url: String, body: Any, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}) = patch(url, returnType, { requestBody(body); withOpts(this) })
    fun <R : Any> patch(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

    // OPTION
    // HEAD
}

data class Request4k(
        val method: HttpMethod4k,
        val url: String,
        val headers: Map<String, String> = emptyMap(),
        // cookies
        val requestBody: DefiniteRequestBody? = null
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

    init {
        // for simplicity sake no perfectly clean design but lazy check for data integrity
        if (!method.isRequestBodySupported && requestBody != null) {
            throw Http4kException("Invalid request! HTTP method [$method] does not support request body: $requestBody")
        }
    }

    override fun toString() = "Request4k(" +
            "method=$method, " +
            "url=$url, " +
            "headers=${filterAuthorizationHeader(headers)}, " +
            //            "cookies=$cookies, " +
            "requestBody=<<$requestBody>>" +
            ")"
}

data class Response4k(
        val statusCode: StatusCode,
        val bodyAsString: String,
        val headers: Map<String, String> = emptyMap()
) {
    companion object // test extensions
}


open class Http4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)

interface GlobalHttp4kConfig :
        BaseUrlConfig,
        BasicAuthConfig,
        HeadersConfig,
        StatusCheckConfig
