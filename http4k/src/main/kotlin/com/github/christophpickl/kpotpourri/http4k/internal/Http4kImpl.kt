package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.DeleteRequestOpts
import com.github.christophpickl.kpotpourri.http4k.GetRequestOpts
import com.github.christophpickl.kpotpourri.http4k.GlobalHttp4kConfig
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.PatchRequestOpts
import com.github.christophpickl.kpotpourri.http4k.PostRequestOpts
import com.github.christophpickl.kpotpourri.http4k.PutRequestOpts
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import kotlin.reflect.KClass


internal class Http4kImpl(
        private val httpImpl: HttpImpl,
        private val globals: GlobalHttp4kConfig
) : Http4k {

    private val log = LOG {}

    override fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: GetRequestOpts.() -> Unit) =
            any(HttpMethod4k.GET, GetRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: PostRequestOpts.() -> Unit) =
            any(HttpMethod4k.POST, PostRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> put(url: String, returnType: KClass<R>, withOpts: PutRequestOpts.() -> Unit) =
            any(HttpMethod4k.PUT, PutRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> delete(url: String, returnType: KClass<R>, withOpts: DeleteRequestOpts.() -> Unit) =
            any(HttpMethod4k.DELETE, DeleteRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> patch(url: String, returnType: KClass<R>, withOpts: PatchRequestOpts.() -> Unit) =
            any(HttpMethod4k.PATCH, PatchRequestOpts(), url, returnType, withOpts)

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
                url = buildUrl(requestOpts, url),
                headers = headers.map,
                requestBody = requestTypeAndBody?.requestBody
        )

        log.debug { "Executing: $request4k" }
        val response4k = httpImpl.execute(request4k)
        log.trace { "response body: <<${response4k.bodyAsString}>>" }
        checkStatusCode(globals.statusCheck, requestOpts.statusCheck, request4k, response4k)
        return castReturnType(response4k, returnType)
    }

    private fun buildUrl(requestOpts: AnyRequestOpts, url: String): String {
        val maybeOverriddenBaseUrl = if (requestOpts.disableBaseUrl) {
            url
        } else {
            globals.baseUrl.combine(url)
        }
        return UrlBuilder.build(maybeOverriddenBaseUrl, requestOpts.queryParams)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R : Any> castReturnType(response: Response4k, returnType: KClass<R>): R =
            when (returnType) {
                Response4k::class -> response as R
                String::class -> response.bodyAsString as R
                else -> mapper.readValue(response.bodyAsString, returnType.java)
            }

}
