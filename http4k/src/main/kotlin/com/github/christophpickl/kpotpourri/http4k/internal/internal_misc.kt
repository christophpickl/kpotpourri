package com.github.christophpickl.kpotpourri.http4k.internal

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.google.common.base.MoreObjects
import java.net.SocketTimeoutException


interface HttpImpl {
    fun execute(request: Request4k): Response4k
}

interface HttpImplFactory {
    fun build(metaMap: MetaMap): HttpImpl
}

internal val mapper = ObjectMapper()
        // or use: @JsonIgnoreProperties(ignoreUnknown = true) for your DTO
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerKotlinModule()

/**
 * Meta data values for concrete implementations.
 */
open class MetaMap {
    protected val map = HashMap<String, Any>()
    operator fun get(key: String) = map[key]

    override fun toString() = MoreObjects.toStringHelper(this)
            .add("map", map)
            .toString()
}

class MutableMetaMap : MetaMap() {

    operator fun plusAssign(pair: Pair<String, Any>) {
        map += pair
    }

}

/**
 * Abstraction when (implementation specific) timeout was triggered (connect, request).
 */
class TimeoutException(message: String, cause: Exception? = null): SocketTimeoutException(message) {
    init {
        cause?.let { initCause(it) }
    }
}
