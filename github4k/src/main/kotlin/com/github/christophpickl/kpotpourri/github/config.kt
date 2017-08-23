package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.HttpProtocol
import com.github.christophpickl.kpotpourri.http4k.ServerConfig

private val log = LOG {}

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

/** The system property name which will be used in #detectGithubPass. */
val githubPassSysprop = "github.pass"

/** Unifies access to get the github password via VM arguments. */
fun detectGithubPass(): String {
    val sysprop = System.getProperty(githubPassSysprop, null) ?:
            throw KPotpourriException("Expected system variable -D$githubPassSysprop to be set!")

    log.debug { "Detected github credentials via system property -D$githubPassSysprop" }
    return sysprop

}
