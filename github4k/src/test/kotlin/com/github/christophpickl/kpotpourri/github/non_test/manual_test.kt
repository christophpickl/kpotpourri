package com.github.christophpickl.kpotpourri.github.non_test

import com.github.christophpickl.kpotpourri.common.collection.prettyPrint
import com.github.christophpickl.kpotpourri.github.GithubConfig
import com.github.christophpickl.kpotpourri.github.buildGithub4k


fun main(args: Array<String>) {
    val github = buildGithub4k(GithubConfig.testRepository)
    github.listOpenMilestones().prettyPrint()
}
