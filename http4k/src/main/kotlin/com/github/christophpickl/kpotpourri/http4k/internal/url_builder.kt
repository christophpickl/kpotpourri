package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.collection.mapsOf
import com.github.christophpickl.kpotpourri.common.web.constructUrl
import com.github.christophpickl.kpotpourri.common.web.parseUrl
import com.github.christophpickl.kpotpourri.http4k.AnyRequestOpts
import com.github.christophpickl.kpotpourri.http4k.GlobalHttp4kConfigurable

/**
 * Combines query params in following order: request > global > url.
 */
internal fun buildUrl(url: String, globals: GlobalHttp4kConfigurable, requestOpts: AnyRequestOpts): String {
    val absoluteUrl = if (requestOpts.disableBaseUrl) url else globals.baseUrl.combine(url)
    val (cleanedUrl, urlParams) = parseUrl(absoluteUrl)
    val allQueryParams = mapsOf(urlParams, globals.queryParams, requestOpts.queryParams)

    return constructUrl(cleanedUrl, allQueryParams)
}
