package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.io.readConfirmation
import java.io.File

fun main(args: Array<String>) {
    release4k {

        baseDirectory = File("/Users/wu/Dev/shiatsu/gadsu_release_playground")

//        initGithub(GithubConfig.testRepository)
        val currentVersion = readVersionFromTxt("src/test/resources/version_1.txt").toVersion1()
        val nextVersion = currentVersion.increment1()


        println("Base directory: ${baseDirectory!!.canonicalPath}")
        println("Current version: ${currentVersion.niceString}")
        println("Next version: ${nextVersion.niceString}")
        readConfirmation(prompt = "Do you wanna release this?")


//        git("status")
//        gradlew("tasks")
//        gradlew("build")

        // TODO read version by user
        // TODO read input (with default) by user (e.g. select milestone)
//        val input = promptUser("Some question?")
//        println("input: $input")

        // TODO read/write version from properties file
        // TODO write version to txt file
        // TODO support snapshots
    }
}
