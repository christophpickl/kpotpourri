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
        val restClient = if(overrideHttpImpl != null) overrideHttpImpl!!
                else RestClientFactory.lookupRestClientByImplementation().build(_implMetaMap)
        return Http4kImpl(restClient, this)
    }
}

interface Http4k {

    fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: GetRequestOpts.() -> Unit = {}): R
    fun get(url: String, withOpts: GetRequestOpts.() -> Unit = {}) =
            get(url, Response4k::class, withOpts)

    fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: PostRequestOpts.() -> Unit = {}): R
    fun post(url: String, withOpts: PostRequestOpts.() -> Unit = {}) =
            post(url, Response4k::class, withOpts)
    fun <R : Any> post(url: String, jacksonObject: Any, returnType: KClass<R>, withOpts: PostRequestOpts.() -> Unit = {}) =
            post(url, returnType, { requestBody(jacksonObject); withOpts(this) })

    fun <R : Any> put(url: String, returnType: KClass<R>, withOpts: PutRequestOpts.() -> Unit = {}): R
    fun put(url: String, withOpts: PutRequestOpts.() -> Unit = {}) =
            put(url, Response4k::class, withOpts)
    fun <R : Any> put(url: String, jacksonObject: Any, returnType: KClass<R>, withOpts: PutRequestOpts.() -> Unit = {}) =
            put(url, returnType, { requestBody(jacksonObject); withOpts(this) })

    fun <R : Any> delete(url: String, returnType: KClass<R>, withOpts: DeleteRequestOpts.() -> Unit = {}): R
    fun delete(url: String, withOpts: DeleteRequestOpts.() -> Unit = {}) =
            delete(url, Response4k::class, withOpts)
    fun <R : Any> delete(url: String, jacksonObject: Any, returnType: KClass<R>, withOpts: DeleteRequestOpts.() -> Unit = {}) =
            delete(url, returnType, { requestBody(jacksonObject); withOpts(this) })

    fun <R : Any> patch(url: String, returnType: KClass<R>, withOpts: PatchRequestOpts.() -> Unit = {}): R
    fun patch(url: String, withOpts: PatchRequestOpts.() -> Unit = {}) =
            patch(url, Response4k::class, withOpts)
    fun <R : Any> patch(url: String, jacksonObject: Any, returnType: KClass<R>, withOpts: PatchRequestOpts.() -> Unit = {}) =
            patch(url, returnType, { requestBody(jacksonObject); withOpts(this) })

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
