package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.GetRequestOpts
import com.github.christophpickl.kpotpourri.http4k.GlobalHttp4kConfig
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.PostRequestOpts
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import kotlin.reflect.KClass


internal class Http4kImpl(
        private val restClient: RestClient,
        private val globals: GlobalHttp4kConfig
) : Http4k {

    private val log = LOG {}

    override fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: GetRequestOpts.() -> Unit) =
            any(HttpMethod4k.GET, GetRequestOpts(), url, returnType, withOpts)

    override fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: PostRequestOpts.() -> Unit) =
            any(HttpMethod4k.POST, PostRequestOpts(), url, returnType, withOpts)

    /**
     * GET, POST, ... or any other.
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
        val fullUrl = UrlBuilder.build(globals.baseUrl.combine(url), requestOpts.queryParams)

        prepareAuthHeader(requestOpts.basicAuth, globals.basicAuth)?.let {
            headers += it
        }

        headers.addAll(globals.headers)
        headers.addAll(requestOpts.headers)

        val request4k = Request4k(
                method = method,
                url = fullUrl,
                headers = headers.map,
                requestBody = requestTypeAndBody?.requestBody
        )

        log.debug { "Executing: $request4k" }
        val response4k = restClient.execute(request4k)
        checkStatusCode(globals.statusCheck, requestOpts.statusCheck, request4k, response4k)
        return castReturnType(response4k, returnType)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R : Any> castReturnType(response: Response4k, returnType: KClass<R>): R =
            when (returnType) {
                Response4k::class -> response as R
                String::class -> response.bodyAsString as R
                else -> mapper.readValue(response.bodyAsString, returnType.java)
            }

}
