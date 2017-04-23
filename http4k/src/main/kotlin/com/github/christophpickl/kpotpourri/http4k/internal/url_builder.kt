package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.GlobalHttp4kConfig
import java.net.URLEncoder


internal object UrlBuilder {

    fun build2(url: String, globals: GlobalHttp4kConfig, requestOpts: AnyRequestOpts): String  {
        val maybeOverriddenBaseUrl = if (requestOpts.disableBaseUrl) {
            url
        } else {
            globals.baseUrl.combine(url)
        }
        val params = buildQueryParams(requestOpts, globals)
        return build(maybeOverriddenBaseUrl, params)
    }

    /**
     * Also takes care if there are already query params set in the URL.
     */
    fun build(url: String, query: Map<String, String>): String {
        if (query.isEmpty()) {
            return url
        }
        val urlSign = if (url.contains("?"))  "&" else "?"
        return url + urlSign + query
                .map { (k, v) -> "${k.urlEencode()}=${v.urlEencode()}" }
                .joinToString("&")
    }

    private fun buildQueryParams(requestOpts: AnyRequestOpts, globals: GlobalHttp4kConfig): MutableMap<String, String> {
        return LinkedHashMap<String, String>().apply {
            putAll(globals.queryParams)
            putAll(requestOpts.queryParams)
        }
    }

    private fun String.urlEencode() = URLEncoder.encode(this, "UTF-8")

}
