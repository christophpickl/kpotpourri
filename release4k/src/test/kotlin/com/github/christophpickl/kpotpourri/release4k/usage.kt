package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.io.readConfirmation

fun main(args: Array<String>) {

    // properties:
    // - project name
    // - git URL
    // - working directory (default: CWD/build/release4k)
    release4k {


//        initGithub(GithubConfig.testRepository)
        val currentVersion = readVersionFromTxt("src/test/resources/version_1.txt").toVersion1()

        val nextVersion = currentVersion.increment1()
        // read version by user as string, or accept his as the default

        // https://github.com/christophpickl/gadsu_release_playground.git


        println("Base directory: ${release4kDirectory.canonicalPath}")
        println("Versions: ${currentVersion.niceString} => ${nextVersion.niceString}")

        // TODO somehow abort now (return is not possible due to lambda nature)
        readConfirmation(prompt = "Do you wanna release this?")

        // FIXME put some stuff in this project: gradlew, and let it create an build artifact (JAR)
        checkoutGitProject("https://github.com/christophpickl/gadsu_release_playground.git")


        // gradlew clean check buildJar -Dversion=NEXT_VERSION
        // change version
        // git add .
        // git commit -m "[Auto-Release] changing version"
        // git tag NEXT_VERSION

        // gradlew bintrayUpload -Dversion=NEXT_VERSION
        // git push ??? needed ???
        // git push origin --tags


//        git("status")
//        gradlew("build")

        // TODO read version by user
        // TODO read input (with default) by user (e.g. select milestone)
        // TODO read/write version from properties file
        // TODO write version to txt file
        // TODO support snapshots

//        val input = promptUser("Some question?")
//        println("input: $input")

    }
}
