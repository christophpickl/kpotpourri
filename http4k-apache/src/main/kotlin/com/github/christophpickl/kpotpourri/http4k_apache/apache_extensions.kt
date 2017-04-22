package com.github.christophpickl.kpotpourri.http4k_apache

import com.github.christophpickl.kpotpourri.http4k.Http4kBuilder
import com.github.christophpickl.kpotpourri.http4k.internal.MetaMap
import com.github.christophpickl.kpotpourri.http4k.internal.MutableMetaMap


fun Http4kBuilder.apacheConnectTimeout(timeoutInMs: Int) {
    _implMetaMap.requestTimeout(timeoutInMs)
}

internal val META_KEY_REQUEST_TIMEOUT = "apache_timeout_request"
internal fun MutableMetaMap.requestTimeout(timeoutInMs: Int) = apply {
    this += META_KEY_REQUEST_TIMEOUT to timeoutInMs
}
internal val MetaMap.requestTimeout: Int? get() = this[META_KEY_REQUEST_TIMEOUT]?.let { it as Int }
