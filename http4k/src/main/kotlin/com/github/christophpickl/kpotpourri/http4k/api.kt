package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.http4k.StatusCheckMode.Anything
import com.github.christophpickl.kpotpourri.http4k.internal.HeadersMap
import com.github.christophpickl.kpotpourri.http4k.internal.Http4kImpl
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClient
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClientFactoryDetector
import com.github.christophpickl.kpotpourri.http4k.internal.MutableMetaMap
import com.github.christophpickl.kpotpourri.http4k.internal.mapper
import kotlin.reflect.KClass

/**
 * Core entry point to build a [Http4k] instance in a DSL falvour.
 */
fun buildHttp4k(withBuilder: Http4kBuilder.() -> Unit = {}): Http4k {
    val builder = Http4kBuilder()
    withBuilder.invoke(builder)
    return builder.end()
}

class Http4kBuilder : GlobalHttp4kConfig {
    override val queryParams: MutableMap<String, String> = HashMap()
    override val headers: HeadersMap = HeadersMap()
    override var baseUrl: BaseUrl = NoBaseUrl
    override var basicAuth: BasicAuthMode = BasicAuthDisabled
    override var statusCheck: StatusCheckMode = Anything

    internal var overrideHttpClient: HttpClient? = null
    val _implMetaMap = MutableMetaMap()

    fun end(): Http4k {
        val restClient = if (overrideHttpClient != null) {
            overrideHttpClient!!
        } else {
            val httpClientFactory = HttpClientFactoryDetector.detect()
            httpClientFactory.build(_implMetaMap)
        }
        return Http4kImpl(restClient, this)
    }
}

// in order to reifie generic type, must not be in an interface
inline fun <reified R : Any> Http4k.get(url: String, noinline withOpts: BodylessRequestOpts.() -> Unit = {}) = getX(url, R::class, withOpts)
inline fun <reified R : Any> Http4k.post(url: String, body: Any = Unit, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) = postX(url, body, R::class, withOpts)
inline fun <reified R : Any> Http4k.put(url: String, body: Any = Unit, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) = putX(url, body, R::class, withOpts)
inline fun <reified R : Any> Http4k.delete(url: String, noinline withOpts: BodylessRequestOpts.() -> Unit = {}) = deleteX(url, R::class, withOpts)
inline fun <reified R : Any> Http4k.patch(url: String, body: Any = Unit, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) = patchX(url, body, R::class, withOpts)

/**
 * Core interface to execute HTTP requests for any method (GET, POST, ...) configurable via request options.
 *
 * return type: either any jackson unmarshallable object, or an [Response4k] instance by default.
 * url: combined with optional global base URL
 * body: something which will be marshalled by jackson
 */
interface Http4k {
    // *X methods ... internal, requiring explicit return type
    // *R methods ... returning a [Response4k] object by default

//    fun get(url: String, withOpts: BodylessRequestOpts.() -> Unit = {}) = get(url, Response4k::class, withOpts)
    fun <R : Any> getX(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit = {}): R

//    fun post(url: String, withOpts: BodyfullRequestOpts.() -> Unit = {}) = post(url, Response4k::class, withOpts)
    fun <R : Any> postR(url: String, body: Any, withOpts: BodyfullRequestOpts.() -> Unit = {}) = postX(url, Response4k::class, { requestBody(body); withOpts(this) })
    fun <R : Any> postX(url: String, body: Any, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}) = postX(url, returnType, { requestBody(body); withOpts(this) })
    fun <R : Any> postX(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

//    fun put(url: String, withOpts: BodyfullRequestOpts.() -> Unit = {}) = put(url, Response4k::class, withOpts)
    fun <R : Any> putR(url: String, body: Any, withOpts: BodyfullRequestOpts.() -> Unit = {}) = putX(url, Response4k::class, { requestBody(body); withOpts(this) })
    fun <R : Any> putX(url: String, body: Any, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}) = putX(url, returnType, { requestBody(body); withOpts(this) })
    fun <R : Any> putX(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

//    fun delete(url: String, withOpts: BodylessRequestOpts.() -> Unit = {}) = delete(url, Response4k::class, withOpts)
    fun <R : Any> deleteX(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit = {}): R

//    fun patch(url: String, withOpts: BodyfullRequestOpts.() -> Unit = {}) = patch(url, Response4k::class, withOpts)
    fun <R : Any> patchR(url: String, body: Any, withOpts: BodyfullRequestOpts.() -> Unit = {}) = patchX(url, Response4k::class, { requestBody(body); withOpts(this) })
    fun <R : Any> patchX(url: String, body: Any, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}) = patchX(url, returnType, { requestBody(body); withOpts(this) })
    fun <R : Any> patchX(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

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

    fun <T : Any> readJson(targetType: KClass<T>): T {
        return mapper.readValue(bodyAsString, targetType.java)
    }
}


open class Http4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)

interface GlobalHttp4kConfig :
        BaseUrlConfig,
        BasicAuthConfig,
        HeadersConfig,
        StatusCheckConfig,
        QueryParamConfig
