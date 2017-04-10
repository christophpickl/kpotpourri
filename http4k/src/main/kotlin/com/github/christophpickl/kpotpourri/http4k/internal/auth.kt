package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.control.takeFirstIfIs
import com.github.christophpickl.kpotpourri.http4k.BasicAuth
import com.github.christophpickl.kpotpourri.http4k.BasicAuthMode
import java.nio.charset.StandardCharsets
import java.util.Base64


internal fun prepareAuthHeader(
        requestAuth: BasicAuthMode,
        globalAuth: BasicAuthMode
): Pair<String, String>? {
    val auth = takeFirstIfIs<BasicAuth>(requestAuth, globalAuth) ?: return null
    return "Authorization" to "Basic ${buildBasicAuthString(auth.username, auth.password)}"
}

private fun buildBasicAuthString(user: String, pass: String): String {
    val message = "$user:$pass".toByteArray(StandardCharsets.UTF_8)
    return Base64.getEncoder().encodeToString(message)
}
