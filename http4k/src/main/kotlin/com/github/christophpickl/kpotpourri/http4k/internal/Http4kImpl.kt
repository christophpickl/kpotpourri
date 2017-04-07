package com.github.christophpickl.kpotpourri.http4k.internal

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.christophpickl.kpotpourri.http4k.DefaultsOptsReadOnly
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.Http4kAnyOpts
import com.github.christophpickl.kpotpourri.http4k.Http4kGetOpts
import com.github.christophpickl.kpotpourri.http4k.Http4kPostOpts
import com.github.christophpickl.kpotpourri.http4k.Http4kWithRequestEntity
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.RequestBody
import com.github.christophpickl.kpotpourri.http4k.Response4k
import kotlin.reflect.KClass

internal class Http4kImpl(
        private val restClient: RestClient,
        private val defaults: DefaultsOptsReadOnly
) : Http4k {

    private val mapper = ObjectMapper()
            // or use: @JsonIgnoreProperties(ignoreUnknown = true) for your DTO
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerKotlinModule()

//    override fun get(url: String, withOpts: Http4kGetOpts.() -> Unit)

    override fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: Http4kGetOpts.() -> Unit) =
            any(url, returnType, withOpts, Http4kGetOpts())

    override fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: Http4kPostOpts.() -> Unit) =
            any(url, returnType, withOpts, Http4kPostOpts())

    /**
     * GET, POST, ... or any other.
     */
    private inline fun <R : Any, reified OPT : Http4kAnyOpts> any(
            url: String,
            returnType: KClass<R>,
            withOpts: OPT.() -> Unit,
            optInstance: OPT
    ): R {
        val requestOpts = optInstance.apply { withOpts(this) }
        val defaultHeaders = HashMap<String, String>()
        val requestBody = prepareBodyAndContentType(requestOpts, defaultHeaders)

//        val auth = requestOpts.basicAuth
//        if (auth is BasicAuth) {
//            val user = requestOpts.basicAuth.username
//        }
        val response = restClient.execute(Request4k(
                method = HttpMethod4k.POST,
                url = defaults.baseUrl.combine(url),
                headers = defaultHeaders.plus(requestOpts.headers),
                requestBody = requestBody
        ))
        return castReturnType(response, returnType)
    }

    private fun authHeaderIfNecessary() {}

    private fun prepareBodyAndContentType(requestOpts: Http4kAnyOpts, headers: MutableMap<String, String>): String? {
        return if (requestOpts is Http4kWithRequestEntity) {
            requestOpts.requestBody.toContentType()?.let {
                headers.put("content-type", it)
            }
            requestOpts.requestBody.toBodyString()
        } else {
            null
        }
    }

    private fun RequestBody.toBodyString(): String? {
        return when (this) {
            is RequestBody.None -> null
            is RequestBody.StringBody -> body
            is RequestBody.JsonBody -> mapper.writeValueAsString(jacksonObject)
        }
    }

    private fun RequestBody.toContentType(): String? {
        return when (this) {
            is RequestBody.None -> null
            is RequestBody.StringBody -> "text/plain"
            is RequestBody.JsonBody -> "application/json"
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R : Any> castReturnType(response: Response4k, returnType: KClass<R>): R =
            when (returnType) {
                Response4k::class -> response as R
                String::class -> response.bodyAsString as R
            // still pass TypeReference instead of kotlin-extension as of some generics hick-hack
//                else -> mapper.readValue(response.bodyAsString, object: TypeReference<R>() {})
                else -> mapper.readValue(response.bodyAsString, returnType.java)
            }

}
