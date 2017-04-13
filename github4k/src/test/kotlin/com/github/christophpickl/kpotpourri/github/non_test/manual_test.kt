package com.github.christophpickl.kpotpourri.github.non_test

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.collection.prettyPrint
import com.github.christophpickl.kpotpourri.github.buildGithub4k

fun main(args: Array<String>) {
    val github = buildGithub4k(GithubConfig.testRepository)

    val milestones = github.listOpenMilestones()
    milestones.prettyPrint()
    github.listTags().prettyPrint()
    github.listIssues(milestones[0]).prettyPrint()
}
