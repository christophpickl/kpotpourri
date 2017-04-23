package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.common.string.toBooleanLenient2
import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.BodyfullRequestOpts
import com.github.christophpickl.kpotpourri.http4k.BodylessRequestOpts
import com.github.christophpickl.kpotpourri.http4k.GlobalHttp4kConfig
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import kotlin.reflect.KClass


internal class Http4kImpl(
        private val httpClient: HttpClient,
        private val globals: GlobalHttp4kConfig
) : Http4k {

    private val log = LOG {}

    override fun <R : Any> getX(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit) =
            any(HttpMethod4k.GET, BodylessRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> postX(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit) =
            any(HttpMethod4k.POST, BodyfullRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> putX(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit) =
            any(HttpMethod4k.PUT, BodyfullRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> deleteX(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit) =
            any(HttpMethod4k.DELETE, BodylessRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> patchX(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit) =
            any(HttpMethod4k.PATCH, BodyfullRequestOpts(), url, returnType, withOpts)

    /**
     * GET, POST, ... or any other. Preparing a [Request4k] instance and passing it to the specific implementation.
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
                url = UrlBuilder.build2(url, globals, requestOpts),
                headers = headers.map,
                requestBody = requestTypeAndBody?.requestBody
        )

        log.debug { "Executing: $request4k" }
        val response4k = httpClient.execute(request4k)
        log.trace { "response body: <<${response4k.bodyAsString}>>" }
        checkStatusCode(globals.statusCheck, requestOpts.statusCheck, request4k, response4k)
        return castReturnType(response4k, returnType)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R : Any> castReturnType(response: Response4k, returnType: KClass<R>): R =
            when (returnType) {
                Response4k::class -> response as R
                String::class -> response.bodyAsString as R
                Any::class -> response as R
                Unit::class -> Unit as R
                // could catch parsing exceptions here ;)
                Float::class -> response.bodyAsString.toFloat() as R
                Double::class -> response.bodyAsString.toDouble() as R
                Byte::class -> response.bodyAsString.toByte() as R
                // ByteArray::class -> ??? as R
                Short::class -> response.bodyAsString.toShort() as R
                Int::class -> response.bodyAsString.toInt() as R
                Long::class -> response.bodyAsString.toLong() as R
                Boolean::class -> response.bodyAsString.toBooleanLenient2() as R
                else -> mapper.readValue(response.bodyAsString, returnType.java)
            }

}
