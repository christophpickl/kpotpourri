package com.github.christophpickl.kpotpourri.http4k.internal

import java.net.URLEncoder


internal object UrlBuilder {

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

    private fun String.urlEencode() = URLEncoder.encode(this, "UTF-8")

}
