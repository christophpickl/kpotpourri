package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.internal.Http4kImpl
import com.github.christophpickl.kpotpourri.http4k.internal.RestClientFactory
import kotlin.reflect.KClass


class Http4kBuilder : DefaultsOpts, DefaultsOptsReadOnly {

    override var baseUrl: BaseUrl = BaseUrl.NoBaseUrl

    fun withDefaults(wither: DefaultsOpts.() -> Unit): Http4kBuilder {
        wither.invoke(this)
        return this
    }

    fun end(): Http4k {
        val restClient = RestClientFactory.lookupRestClientByImplementation()
        return Http4kImpl(restClient, this)
    }
}

fun buildHttp4k() = Http4kBuilder()

/**
 * Got no body, opposed to POST/PUT requests.
 */
data class Http4kGetOpts (
    val headers: MutableMap<String, String> = HashMap() // TODO make multi value map
)

interface Http4k {
    fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: Http4kGetOpts.() -> Unit): R
}

data class Response4k(
        // header
        // cookies
        val statusCode: Int,
        val bodyAsString: String
)

enum class HttpMethod4k {
    GET
}
