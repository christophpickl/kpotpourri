package com.github.christophpickl.kpotpourri.github.non_test

import com.github.christophpickl.kpotpourri.common.collection.prettyPrint
import com.github.christophpickl.kpotpourri.github.RepositoryConfig
import com.github.christophpickl.kpotpourri.github.buildGithub4k


fun main(args: Array<String>) {
    val github = buildGithub4k(RepositoryConfig.testRepository)
    github.listOpenMilestones().prettyPrint()
}

fun usageDemo() {
    val github4k = buildGithub4k(RepositoryConfig(
            repositoryOwner = "repoOwner",
            repositoryName = "repoName",
            username = "user",
            password = "pass"
    ))

    val milestone = github4k.listOpenMilestones().first()
    val issues = github4k.listIssues(milestone)
    println(issues.first().title)

    // github4k.close(milestone)
    // github4k.listTags()
    // github4k.uploadReleaseAsset()
}
