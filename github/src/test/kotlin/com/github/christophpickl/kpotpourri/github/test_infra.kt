package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.common.KPotpourriException

val GithubConfig.Companion.testRepository: GithubConfig get() = GithubConfig(
        repositoryName = "gadsu_release_playground",
        repositoryOwner = "christophpickl",
        username = "christoph.pickl@gmail.com",
        password = System.getProperty("github.pass", null) ?: throw KPotpourriException("Expected system variable -Dgithub.pass")
)
