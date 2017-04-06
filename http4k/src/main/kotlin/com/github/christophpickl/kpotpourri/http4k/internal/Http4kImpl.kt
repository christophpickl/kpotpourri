package com.github.christophpickl.kpotpourri.http4k.internal

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.christophpickl.kpotpourri.http4k.DefaultsOptsReadOnly
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.Http4kGetOpts
import com.github.christophpickl.kpotpourri.http4k.Http4kPostOpts
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

    override fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: Http4kGetOpts.() -> Unit): R {
        val requestOpts = Http4kGetOpts().apply { withOpts(this) }

        val response = restClient.execute(Request4k(
                method = HttpMethod4k.GET,
                url = defaults.baseUrl.combine(url),
                headers = requestOpts.headers
        ))

        return castReturnType(response, returnType)
    }

    override fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: Http4kPostOpts.() -> Unit): R {
        val requestOpts = Http4kPostOpts().apply { withOpts(this) }

        val defaultHeaders = HashMap<String, String>()
        requestOpts.requestBody.toContentType()?.let {
            defaultHeaders.put("content-type", it)
        }

        val response = restClient.execute(Request4k(
                method = HttpMethod4k.POST,
                url = defaults.baseUrl.combine(url),
                headers = defaultHeaders.plus(requestOpts.headers),
                requestBody = requestOpts.requestBody.toBodyString()
        ))
        return castReturnType(response, returnType)
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
