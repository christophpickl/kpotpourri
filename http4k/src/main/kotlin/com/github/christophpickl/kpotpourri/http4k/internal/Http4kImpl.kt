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
        private val defaults: GlobalHttp4kConfig
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

        val defaultHeaders = HashMap<String, String>()
        val requestTypeAndBody = prepareBodyAndContentType(requestOpts)
        if (requestTypeAndBody != null) {
            defaultHeaders += "Content-Type" to requestTypeAndBody.contentType
        }
        val fullUrl = UrlBuilder.build(defaults.baseUrl.combine(url), requestOpts.queryParams)

        prepareAuthHeader(requestOpts.basicAuth, defaults.basicAuth)?.let {
            defaultHeaders += it
        }

        val request4k = Request4k(
                method = method,
                url = fullUrl,
                headers = defaultHeaders.plus(requestOpts.headers),
                requestBody = requestTypeAndBody?.requestBody
        )

        log.debug { "Executing: $request4k" }
        val response4k = restClient.execute(request4k)
        checkStatusCode(requestOpts, request4k, response4k)
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
