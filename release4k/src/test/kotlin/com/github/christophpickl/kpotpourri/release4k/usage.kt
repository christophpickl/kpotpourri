package com.github.christophpickl.kpotpourri.release4k

import java.io.File

fun main(args: Array<String>) {
    release4k {

        baseDirectory = File("/Users/wu/Dev/shiatsu/gadsu_release_playground")

//        initGithub(GithubConfig.testRepository)
//        val version = readVersionFromTxt("src/test/resources/version_1.txt")
//        git("status")
//        gradlew("tasks")

        gradlew("build")

        // TODO confirm: [y/N]
        // TODO read version by user
        // TODO read input (with default) by user (e.g. select milestone)
//        val input = promptUser("Some question?")
//        println("input: $input")

        // TODO read/write version from properties file
        // TODO write version to txt file
        // TODO support snapshots

    }
}
