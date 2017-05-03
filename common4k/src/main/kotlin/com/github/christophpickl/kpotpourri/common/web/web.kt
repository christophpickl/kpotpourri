package com.github.christophpickl.kpotpourri.common.web

import java.net.URLEncoder

/**
 * URL query params type.
 */
typealias QueryParams = Map<String, String>

/**
 * Extracts possible query params from given URL and returns query params cleaned URL.
 */
fun parseUrl(url: String): Pair<String, QueryParams> {
    if (!url.contains("?")) {
        return url to emptyMap()
    }
    val questionPos = url.indexOf("?")
    val paramsInUrl = url.substring(questionPos + 1)
            .split("&")
            .filter(String::isNotEmpty)
            .map { it.split("=").let { Pair(it[0], it[1]) } }.toMap()

    return url.substring(0, questionPos) to paramsInUrl
}

/**
 * Appends query params to URL, whereas the URL may or may not already contain query params.
 */
fun constructUrl(url: String, queryParams: QueryParams): String {
    if (queryParams.isEmpty()) {
        return url
    }
    val urlSign = if (url.contains("?")) "&" else "?"
    return url + urlSign + queryParams
            .map { (k, v) -> "${k.urlEencode()}=${v.urlEencode()}" }
            .joinToString("&")
}

/**
 * Simplify usage of `URLEncoder.encode()` via extension method only.
 */
fun String.urlEencode() = URLEncoder.encode(this, "UTF-8")
