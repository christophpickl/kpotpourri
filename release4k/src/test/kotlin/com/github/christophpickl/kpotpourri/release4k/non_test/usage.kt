package com.github.christophpickl.kpotpourri.release4k.non_test

import com.github.christophpickl.kpotpourri.common.io.Keyboard
import com.github.christophpickl.kpotpourri.release4k.Version.VersionParts2.Companion.readVersion2FromStdin
import com.github.christophpickl.kpotpourri.release4k.release4k
import java.io.File

fun main(args: Array<String>) = release4k {
    val versionTxtFilename = "version.txt"
    val gitUrl = "https://github.com/christophpickl/kpotpourri.git"

    // =================================================================================================================

    val currentVersion = readVersionFromTxt(versionTxtFilename).toVersion2()
    val nextVersion = readVersion2FromStdin(
            prompt = "Enter next release version",
            defaultVersion = currentVersion.incrementMinor()
    )
    val nextVersionString = nextVersion.niceString
    val syspropNextVersion = "-Dkpotpourri.version=$nextVersionString"

    // =================================================================================================================
    printHeader("VERIFY NO CHANGES")
    git(listOf("status"))
    println()
    if (!Keyboard.readConfirmation(prompt = "Are you sure there are no changes and everything was pushed?!")) {
        return
    }

    // =================================================================================================================
    printHeader("RELEASE NOTES")
    println("Base release directory: ${release4kDirectory.canonicalPath}")
    println("GitHub URL: $gitUrl")
    println("Version file: ${File(versionTxtFilename).canonicalPath}")
    println("Versions: ${currentVersion.niceString} => $nextVersionString")
    println()

    // =================================================================================================================
    if (!Keyboard.readConfirmation(prompt = "Do you wanna release this?")) {
        return
    }

    // =================================================================================================================
    printHeader("GIT CHECKOUT")
    checkoutGitProject(gitUrl)

    // =================================================================================================================
    printHeader("GRADLE BUILD")
    gradlew(listOf("clean","check","checkTodo","test","build", syspropNextVersion))

    // =================================================================================================================
    printHeader("CHANGE VERSION")
    File(gitCheckoutDirectory, versionTxtFilename).writeText(nextVersionString)

    // =================================================================================================================
    printHeader("GIT COMMIT")
    git(listOf("status"))
    git(listOf("add","."))
    git(listOf("commit","-m", "[Auto-Release] Preparing release $nextVersionString"))
    git(listOf("tag", nextVersionString))

    git(listOf("push"))
    git(listOf("push", "origin", "--tags"))

}
