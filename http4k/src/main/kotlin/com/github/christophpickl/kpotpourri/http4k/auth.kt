package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.BasicAuthMode.BasicAuth


interface BasicAuthConfigurable {

    var basicAuth: BasicAuthMode

    fun basicAuth(username: String, password: String) {
        basicAuth = BasicAuth(username, password)
    }

}


sealed class BasicAuthMode {

    object BasicAuthDisabled : BasicAuthMode()

    data class BasicAuth(
            val username: String,
            val password: String
    ) : BasicAuthMode()

}
