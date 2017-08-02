package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.http4k.HttpProtocol
import com.github.christophpickl.kpotpourri.http4k.ServerConfig

/**
 * Official GitHub.com server configuration: https://api.github.com:443
 */
val GITHUB_COM = ServerConfig(
        protocol = HttpProtocol.Https,
        hostName = "api.github.com",
        port = 443
)

/**
 * Defines the access to a certain GitHub repository.
 */
data class RepositoryConfig(
        val repositoryOwner: String,
        val repositoryName: String,
        val username: String,
        val password: String
) {
    companion object // for test extensions
}
