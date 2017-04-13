package com.github.christophpickl.kpotpourri.http4k_apache

import com.github.christophpickl.kpotpourri.http4k.Http4kBuilder
import com.github.christophpickl.kpotpourri.http4k.internal.MetaMap
import com.github.christophpickl.kpotpourri.http4k.internal.MutableMetaMap


fun Http4kBuilder.apacheConnectTimeout(timeoutInMs: Int) {
    _implMetaMap.connectTimeout(timeoutInMs)
}

internal val META_KEY_CONNECT_TIMEOUT = "apache_connect_timeout"
internal fun MutableMetaMap.connectTimeout(timeoutInMs: Int) {
    this += META_KEY_CONNECT_TIMEOUT to timeoutInMs
}
internal val MetaMap.connectTimeout: Int? get() = this[META_KEY_CONNECT_TIMEOUT]?.let { it as Int }
