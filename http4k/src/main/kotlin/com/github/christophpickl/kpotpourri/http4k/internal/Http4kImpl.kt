package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.DefaultsOptsReadOnly
import com.github.christophpickl.kpotpourri.http4k.Http4k
import com.github.christophpickl.kpotpourri.http4k.Http4kGetOpts
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import kotlin.reflect.KClass

internal class Http4kImpl(
        private val restClient: RestClient,
        private val defaults: DefaultsOptsReadOnly
) : Http4k {

    override fun get(url: String, withOpts: Http4kGetOpts.() -> Unit) = get(url, Response4k::class, withOpts)

    override fun <R : Any> get(url: String, returnType: KClass<R>, withOpts: Http4kGetOpts.() -> Unit): R {
        val getOpts = Http4kGetOpts()
        withOpts.invoke(getOpts)

        val response = restClient.execute(Request4k(
                method = HttpMethod4k.GET,
                url = defaults.baseUrl.combine(url),
                headers = getOpts.headers
        ))

        return castReturnType(response, returnType)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R: Any> castReturnType(response: Response4k, returnType: KClass<R>) =
        when (returnType) {
            Response4k::class -> response as R
            String::class -> response.bodyAsString as R
            else -> null as R // TODO object mapper
        }

}
