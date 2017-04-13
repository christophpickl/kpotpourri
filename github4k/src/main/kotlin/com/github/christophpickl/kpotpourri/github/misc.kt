package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.common.KPotpourriException


class Github4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)

data class GithubConfig(
        val repositoryOwner: String,
        val repositoryName: String,
        val username: String,
        val password: String
) {
    companion object // for test extensions
}
