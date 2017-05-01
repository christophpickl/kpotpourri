package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.tomakehurst.wiremock.http.Request

/**
 * Checks if the the given header is present (multiple header values supported).
 */
fun Request.hasHeader(pair: Pair<String, String>) =
        headers.getHeader(pair.first).containsValue(pair.second)
