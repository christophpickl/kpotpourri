package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.collection.prettyPrint

fun main(args: Array<String>) {
    val github = buildGithub4k(GithubConfig(
            repositoryName = "gadsu_release_playground",
            repositoryOwner = "christophpickl",
            username = "christoph.pickl@gmail.com",
            password = System.getProperty("github.pass", null) ?: throw KPotpourriException("Expected system variable -Dgithub.pass")
    ))

    val milestones = github.listOpenMilestones()
    milestones.prettyPrint()
    github.listTags().prettyPrint()
    github.listIssues(milestones[0]).prettyPrint()
}
