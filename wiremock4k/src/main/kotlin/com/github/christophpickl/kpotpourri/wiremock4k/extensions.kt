package com.github.christophpickl.kpotpourri.wiremock4k

import com.github.tomakehurst.wiremock.http.Request

// MINOR test me
fun Request.hasHeader(pair: Pair<String, String>) =
        header(pair.first).values().firstOrNull()?.equals(pair.second) ?: false
