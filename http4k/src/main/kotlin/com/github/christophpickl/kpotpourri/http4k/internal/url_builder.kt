package com.github.christophpickl.kpotpourri.http4k.internal

import java.net.URLEncoder


internal object UrlBuilder {
    fun build(url: String, query: Map<String, String>): String {
        if (query.isEmpty()) {
            return url
        }
        return url + "?" + query
                .map { (k, v) -> "${k.urlEencode()}=${v.urlEencode()}" }
                .joinToString("&")
    }

    private fun String.urlEencode() = URLEncoder.encode(this, "UTF-8")

}
