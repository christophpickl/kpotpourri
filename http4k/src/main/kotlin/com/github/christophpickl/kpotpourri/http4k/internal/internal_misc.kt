package com.github.christophpickl.kpotpourri.http4k.internal

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k


interface HttpImpl {
    fun execute(request: Request4k): Response4k
}

internal val mapper = ObjectMapper()
        // or use: @JsonIgnoreProperties(ignoreUnknown = true) for your DTO
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerKotlinModule()
