package com.github.christophpickl.kpotpourri.http4k.internal

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.christophpickl.kpotpourri.http4k.BasicAuth
import com.github.christophpickl.kpotpourri.http4k.DefaultsOpts
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.Http4kAnyOpts
import com.github.christophpickl.kpotpourri.http4k.Http4kGetOpts
import com.github.christophpickl.kpotpourri.http4k.Http4kPostOpts
import com.github.christophpickl.kpotpourri.http4k.Http4kWithRequestEntity
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.RequestBody
import com.github.christophpickl.kpotpourri.http4k.Response4k
import java.nio.charset.StandardCharsets
import java.util.Base64
import kotlin.reflect.KClass

internal class Http4kImpl(
        private val restClient: RestClient,
        private val defaults: DefaultsOpts
) : Http4k {

    private val mapper = ObjectMapper()
            // or use: @JsonIgnoreProperties(ignoreUnknown = true) for your DTO
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerKotlinModule()

//    override fun get(url: String, withOpts: Http4kGetOpts.() -> Unit)

    override fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: Http4kGetOpts.() -> Unit) =
            any(HttpMethod4k.GET, Http4kGetOpts(), url, returnType, withOpts)

    override fun <R : Any> post(url: String, returnType: KClass<R>, withOpts: Http4kPostOpts.() -> Unit) =
            any(HttpMethod4k.POST, Http4kPostOpts(), url, returnType, withOpts)

    /**
     * GET, POST, ... or any other.
     */
    private inline fun <R : Any, reified OPT : Http4kAnyOpts> any(
            method: HttpMethod4k,
            optInstance: OPT,
            url: String,
            returnType: KClass<R>,
            withOpts: OPT.() -> Unit
    ): R {
        val requestOpts = optInstance.apply { withOpts(this) }
        val defaultHeaders = HashMap<String, String>()
        val requestBody = prepareBodyAndContentType(requestOpts, defaultHeaders)

        authHeaderIfNecessary(requestOpts, defaultHeaders)
        val response = restClient.execute(Request4k(
                method = method,
                url = defaults.baseUrl.combine(url),
                headers = defaultHeaders.plus(requestOpts.headers),
                requestBody = requestBody
        ))
        return castReturnType(response, returnType)
    }

    private fun authHeaderIfNecessary(requestOpts: Http4kAnyOpts, headers: MutableMap<String, String>) {
        val globalAuth = defaults.basicAuth
        val requestAuth = requestOpts.basicAuth
        val auth = if (globalAuth is BasicAuth && requestAuth is BasicAuth) {
            // global auth is overridden by request auth
            requestAuth
        } else if (globalAuth is BasicAuth) {
            globalAuth
        } else if (requestAuth is BasicAuth) {
            requestAuth
        } else {
            null
        } ?: return

        headers += "Authorization" to "Basic ${buildBasicAuthString(auth.username, auth.password)}"
    }

    private fun buildBasicAuthString(user: String, pass: String): String {
        val message = "$user:$pass".toByteArray(StandardCharsets.UTF_8)
        return Base64.getEncoder().encodeToString(message)
    }

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
