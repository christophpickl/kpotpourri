package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.common.string.toBooleanLenient2
import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.BodyfullRequestOpts
import com.github.christophpickl.kpotpourri.http4k.BodylessRequestOpts
import com.github.christophpickl.kpotpourri.http4k.GlobalHttp4kConfigurable
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import kotlin.reflect.KClass


internal class Http4kImpl(
        private val httpClient: HttpClient,
        private val globals: GlobalHttp4kConfigurable
) : Http4k {

    private val log = LOG {}

    override fun <R : Any> getReturning(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit) =
            any(HttpMethod4k.GET, BodylessRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> postReturning(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit) =
            any(HttpMethod4k.POST, BodyfullRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> putReturning(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit) =
            any(HttpMethod4k.PUT, BodyfullRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> deleteReturning(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit) =
            any(HttpMethod4k.DELETE, BodylessRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> patchReturning(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit) =
            any(HttpMethod4k.PATCH, BodyfullRequestOpts(), url, returnType, withOpts)

    /**
     * GET, POST, ... or any other. Preparing a [Request4k] instance and passing it to the specific implementation.
     *
     * @param url may or may not have already query params set
     */
    private inline fun <R : Any, reified OPT : AnyRequestOpts> any(
            method: HttpMethod4k,
            optInstance: OPT,
            url: String,
            returnType: KClass<R>,
            withOpts: OPT.() -> Unit
    ): R {
        val requestOpts = optInstance.apply { withOpts(this) }

        val headers = HeadersMap()
        val requestTypeAndBody = prepareBodyAndContentType(requestOpts)
        if (requestTypeAndBody != null) {
            headers += "Content-Type" to requestTypeAndBody.contentType
        }
        prepareAuthHeader(requestOpts.basicAuth, globals.basicAuth)?.let {
            headers += it
        }
        headers.addAll(globals.headers)
        headers.addAll(requestOpts.headers)

        val request4k = Request4k(
                method = method,
                url = buildUrl(url, globals, requestOpts),
                headers = headers.map,
                requestBody = requestTypeAndBody?.requestBody
        )

        log.debug { "Executing: $request4k" }
        val response4k = httpClient.execute(request4k)
        log.trace { "response body: <<${response4k.bodyAsString}>>" }
        checkStatusCode(globals.statusCheck, requestOpts.statusCheck, request4k, response4k)

        return response4k.castTo(returnType)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R : Any> Response4k.castTo(returnType: KClass<R>): R =
            when (returnType) {
                Response4k::class -> this as R
                String::class -> this.bodyAsString as R
                Any::class -> this as R
                Unit::class -> Unit as R
                // could catch parsing exceptions here ;)
                Float::class -> this.bodyAsString.toFloat() as R
                Double::class -> this.bodyAsString.toDouble() as R
                Byte::class -> this.bodyAsString.toByte() as R
                // ByteArray::class -> ??? as R
                Short::class -> this.bodyAsString.toShort() as R
                Int::class -> this.bodyAsString.toInt() as R
                Long::class -> this.bodyAsString.toLong() as R
                Boolean::class -> this.bodyAsString.toBooleanLenient2() as R
                else -> mapper.readValue(this.bodyAsString, returnType.java)
            }

}
