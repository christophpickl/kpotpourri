package com.github.christophpickl.kpotpourri.http4k


interface BasicAuthConfigurable {

    var basicAuth: BasicAuthMode

    fun basicAuth(username: String, password: String) {
        basicAuth = BasicAuth(username, password)
    }
}


sealed class BasicAuthMode

object BasicAuthDisabled : BasicAuthMode()

data class BasicAuth(
        val username: String,
        val password: String
) : BasicAuthMode()
