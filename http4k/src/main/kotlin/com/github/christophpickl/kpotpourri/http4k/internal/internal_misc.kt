package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4kMapper
import com.google.common.base.MoreObjects
import java.net.SocketTimeoutException


interface HttpClient {
    fun execute(request4k: Request4k): Response4k
}

interface HttpClientFactory {
    fun build(metaMap: MetaMap): HttpClient
}

internal val mapper = buildJackson4kMapper()

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
        cause?.let(this::initCause)
    }
}
