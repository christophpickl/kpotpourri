package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.io.readConfirmation
import com.github.christophpickl.kpotpourri.release4k.Version.VersionParts2.Companion.readVersion2FromStdin
import java.io.File

fun main(args: Array<String>) = release4k {
    if (release4kDirectory.canonicalPath.endsWith("release4k")) {
        throw RuntimeException("Execute this main class from 'kpotpourri' root directory, instead of submodule 'release4k' ;)")
    }

    val versionTxt = "version.txt"

    // initGithub(GithubConfig.testRepository)

    val currentVersion = readVersionFromTxt(versionTxt).toVersion2()
    val nextVersion = readVersion2FromStdin(defaultVersion = currentVersion.incrementMinor())
    val nextVersionString = nextVersion.niceString

    printHeader("VERIFY NO CHANGES")
    execute("git", "status", File(""))
    if (!readConfirmation(prompt = "Are you sure there are no changes and everything was pushed?!")) {
        return
    }

    printHeader("RELEASE NOTES")

    println("Base release directory: ${release4kDirectory.canonicalPath}")
    println("Version file: ${File(versionTxt).canonicalPath}")
    println("Versions: ${currentVersion.niceString} => $nextVersionString")

    if (!readConfirmation(prompt = "Do you wanna release this?")) {
        return
    }

    printHeader("CHECKOUT GIT")
    checkoutGitProject("https://github.com/christophpickl/kpotpourri.git")

    printHeader("GRADLE BUILD")
    // gradlew clean check buildJar -Dversion=NEXT_VERSION
    gradlew("clean check buildJar -Dversion=$nextVersionString")

    printHeader("CHANGE VERSION")
    writeVersionToTxt(versionTxt, nextVersion)

    git("status")
    git("add .")
    git("commit -m \"[Auto-Release] Changing version to $nextVersionString\"")
    git("tag $nextVersionString")

    printHeader("BINTRAY UPLOAD")
    gradlew("bintrayUpload -Dversion=$nextVersionString")
    // git push ??? needed ???
    git("push origin --tags")
}
