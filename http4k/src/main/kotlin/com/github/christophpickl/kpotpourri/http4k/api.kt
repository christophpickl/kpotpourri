package com.github.christophpickl.kpotpourri.http4k

import com.fasterxml.jackson.core.type.TypeReference
import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.http4k.BasicAuthMode.BasicAuthDisabled
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

/**
 * Http4k can be globally configured consisting of the following interfaces.
 */
interface GlobalHttp4kConfigurable :
        BaseUrlConfigurable,
        BasicAuthConfigurable,
        HeadersConfigurable,
        StatusCheckConfigurable,
        QueryParamConfigurable

/**
 * Core DSL class to prepare a http4k instance, configuring global options (which can be overridden per request).
 */
class Http4kBuilder : GlobalHttp4kConfigurable {

    /** Global URL query parameters for each request. */
    override val queryParams: MutableMap<String, String> = HashMap()

    /** Global HTTP headers. */
    override val headers: HeadersMap = HeadersMap()

    /** By default the base URL is not defined/used and reach request must be an absolute URL. */
    override var baseUrl: BaseUrl = NoBaseUrl

    /** By default basic authentication is disabled. */
    override var basicAuth: BasicAuthMode = BasicAuthDisabled

    /** By default any HTTP status code is ok and will not throw an exception (depending on concrete HTTP implementation). */
    override var statusCheck: StatusCheckMode = Anything

    internal var overrideHttpClient: HttpClient? = null
    /** Actually not visible to the outside, but still must be public for per-implementation extensions. */
    val _implMetaMap = MutableMetaMap()

    /**
     * Detects (or using overridden) HTTP client implementation based on classpath availability.
     */
    fun end(): Http4k {
        val restClient = if (overrideHttpClient != null) {
            overrideHttpClient!!
        } else {
            val httpClientFactory = HttpClientFactoryDetector().detect()
            httpClientFactory.build(_implMetaMap)
        }
        return Http4kImpl(restClient, this)
    }
}

// in order to reifie generic type, must not be in an interface
/** Reified version of GET. */
inline fun <reified R : Any> Http4k.get(url: String, noinline withOpts: BodylessRequestOpts.() -> Unit = {}) = getReturning(url, R::class, withOpts)

/** Reified version of POST. */
inline fun <reified R : Any> Http4k.post(url: String, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) = postReturning(url, R::class, withOpts)

/** Reified version of PUT. */
inline fun <reified R : Any> Http4k.put(url: String, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) = putReturning(url, R::class, withOpts)

/** Reified version of DELETE. */
inline fun <reified R : Any> Http4k.delete(url: String, noinline withOpts: BodylessRequestOpts.() -> Unit = {}) = deleteReturning(url, R::class, withOpts)

/** Reified version of PATCH. */
inline fun <reified R : Any> Http4k.patch(url: String, noinline withOpts: BodyfullRequestOpts.() -> Unit = {}) = patchReturning(url, R::class, withOpts)

/**
 * Core interface to execute HTTP requests for any method (GET, POST, ...) configurable via request options.
 *
 * return type: either any jackson unmarshallable object, or an [Response4k] instance by default.
 * url: combined with optional global base URL
 * body: something which will be marshalled by jackson
 */
interface Http4k {

    /** GET response with explicity return type. */
    fun <R : Any> getReturning(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit = {}): R

    /** GET response for generic return types. */
    fun <R : Any> getGeneric(url: String, returnType: TypeReference<R>, withOpts: BodylessRequestOpts.() -> Unit = {}): R

    /** POST response with return type set to [Response4k]. */
    fun <R : Any> postAndReturnResponse(url: String, withOpts: BodyfullRequestOpts.() -> Unit = {}) = postReturning(url, Response4k::class, { withOpts(this) })

    /** POST response with explicity return type. */
    fun <R : Any> postReturning(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

    /** POST response for generic return types. */
    fun <R : Any> postGeneric(url: String, returnType: TypeReference<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

    /** PUT response with return type set to [Response4k]. */
    fun <R : Any> putAndReturnResponse(url: String, body: Any, withOpts: BodyfullRequestOpts.() -> Unit = {}) = putReturning(url, Response4k::class, { requestBody(body); withOpts(this) })

    /** PUT response with explicity return type. */
    fun <R : Any> putReturning(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

    /** PUT response for generic return types. */
    fun <R : Any> putGeneric(url: String, returnType: TypeReference<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

    /** DELETE response with explicity return type. */
    fun <R : Any> deleteReturning(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit = {}): R

    /** DELETE response for generic return types. */
    fun <R : Any> deleteGeneric(url: String, returnType: TypeReference<R>, withOpts: BodylessRequestOpts.() -> Unit = {}): R

    /** PATCH response with return type set to [Response4k]. */
    fun <R : Any> patchAndReturnResponse(url: String, withOpts: BodyfullRequestOpts.() -> Unit = {}) = patchReturning(url, Response4k::class, { withOpts(this) })

    /** PATCH response with explicity return type. */
    fun <R : Any> patchReturning(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

    /** PATCH response for generic return types. */
    fun <R : Any> patchGeneric(url: String, returnType: TypeReference<R>, withOpts: BodyfullRequestOpts.() -> Unit = {}): R

}

/**
 * Core request object abstraction.
 */
data class Request4k(
        val method: HttpMethod4k,
        val url: String,
        val headers: Map<String, String> = emptyMap(),
        val requestBody: DefiniteRequestBody? = null
) {

    companion object {}// for extensions

    init {
        // for simplicity sake no perfectly clean design but lazy check for data integrity
        if (!method.isRequestBodySupported && requestBody != null) {
            throw Http4kException("Invalid request! HTTP method [$method] does not support request body: $requestBody")
        }
    }

    /**
     * Don't render authorization header.
     */
    override fun toString() = "Request4k(" +
            "method=$method, " +
            "url=$url, " +
            "headers=${headerWithoutAuthorizationSecret()}, " +
            "requestBody=<<$requestBody>>" +
            ")"

    private fun headerWithoutAuthorizationSecret(): Map<String, String> {
        val newHeaders = HashMap<String, String>(headers.size)
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

/**
 * Core response object abstraction.
 */
data class Response4k(
        /** HTTP status code. */
        val statusCode: StatusCode,
        /** Eagerly response body as a string. */
        val bodyAsString: String,
        /** Response headers, obviously. */
        val headers: Map<String, String> = emptyMap()
) {
    companion object // test extensions

    /**
     * Transform the response body to a custom JSON object.
     */
    fun <T : Any> readJson(targetType: KClass<T>): T { // MINOR could do also here an annotation check (if option is enabled to do so)
        return mapper.readValue(bodyAsString, targetType.java)
    }
}

/**
 * Global custom exception type.
 */
open class Http4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)
