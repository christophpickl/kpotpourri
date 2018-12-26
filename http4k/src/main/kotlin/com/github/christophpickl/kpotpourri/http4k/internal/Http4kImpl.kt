package com.github.christophpickl.kpotpourri.http4k.internal

import com.fasterxml.jackson.core.type.TypeReference
import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.BodyfullRequestOpts
import com.github.christophpickl.kpotpourri.http4k.BodylessRequestOpts
import com.github.christophpickl.kpotpourri.http4k.GlobalHttp4kConfigurable
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import mu.KotlinLogging.logger
import kotlin.reflect.KClass

internal class Http4kImpl(
        private val httpClient: HttpClient,
        private val globals: GlobalHttp4kConfigurable
) : Http4k {

    private val log = logger {}

    override fun <R : Any> getReturning(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit): R =
            any(HttpMethod4k.GET, BodylessRequestOpts(), url, returnType.toOption(), withOpts)

    override fun <R : Any> getGeneric(url: String, returnType: TypeReference<R>, withOpts: BodylessRequestOpts.() -> Unit): R =
            any(HttpMethod4k.GET, BodylessRequestOpts(), url, returnType.toOption(), withOpts)

    override fun <R : Any> postReturning(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit): R =
            any(HttpMethod4k.POST, BodyfullRequestOpts(), url, returnType.toOption(), withOpts)

    override fun <R : Any> postGeneric(url: String, returnType: TypeReference<R>, withOpts: BodyfullRequestOpts.() -> Unit): R =
            any(HttpMethod4k.POST, BodyfullRequestOpts(), url, returnType.toOption(), withOpts)

    override fun <R : Any> putReturning(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit): R =
            any(HttpMethod4k.PUT, BodyfullRequestOpts(), url, returnType.toOption(), withOpts)

    override fun <R : Any> putGeneric(url: String, returnType: TypeReference<R>, withOpts: BodyfullRequestOpts.() -> Unit): R =
            any(HttpMethod4k.PUT, BodyfullRequestOpts(), url, returnType.toOption(), withOpts)

    override fun <R : Any> deleteReturning(url: String, returnType: KClass<R>, withOpts: BodylessRequestOpts.() -> Unit): R =
            any(HttpMethod4k.DELETE, BodylessRequestOpts(), url, returnType.toOption(), withOpts)

    override fun <R : Any> deleteGeneric(url: String, returnType: TypeReference<R>, withOpts: BodylessRequestOpts.() -> Unit): R =
            any(HttpMethod4k.DELETE, BodylessRequestOpts(), url, returnType.toOption(), withOpts)

    override fun <R : Any> patchReturning(url: String, returnType: KClass<R>, withOpts: BodyfullRequestOpts.() -> Unit): R =
            any(HttpMethod4k.PATCH, BodyfullRequestOpts(), url, returnType.toOption(), withOpts)

    override fun <R : Any> patchGeneric(url: String, returnType: TypeReference<R>, withOpts: BodyfullRequestOpts.() -> Unit): R =
            any(HttpMethod4k.PATCH, BodyfullRequestOpts(), url, returnType.toOption(), withOpts)

    /**
     * GET, POST, ... or any other. Preparing a [Request4k] instance and passing it to the specific implementation.
     *
     * @param url may or may not have already query params set
     */
    private inline fun <R : Any, reified OPT : AnyRequestOpts> any(
            method: HttpMethod4k,
            optInstance: OPT,
            url: String,
            returnOption: ReturnOption<R>,
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

        log.debug { "Executing request: $request4k" }
        val response4k = httpClient.execute(request4k)
        log.trace { "Response body: <<${response4k.bodyAsString}>>" }
        checkStatusCode(globals.statusCheck, requestOpts.statusCheck, request4k, response4k)

        return ResponseCaster.cast(response4k, returnOption)
    }

}
