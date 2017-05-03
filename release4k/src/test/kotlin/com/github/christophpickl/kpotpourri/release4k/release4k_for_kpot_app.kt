package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.io.Keyboard
import com.github.christophpickl.kpotpourri.release4k.Version.VersionParts2.Companion.readVersion2FromStdin
import java.io.File

fun main(args: Array<String>) = release4k {
    println()
    println("""
 _                _                               _
| | ___ __   ___ | |_ _ __   ___  _   _ _ __ _ __(_)
| |/ / '_ \ / _ \| __| '_ \ / _ \| | | | '__| '__| |
|   <| |_) | (_) | |_| |_) | (_) | |_| | |  | |  | |
|_|\_\ .__/ \___/ \__| .__/ \___/ \__,_|_|  |_|  |_|
     |_|             |_|
""")
    println()
    if (File("").canonicalPath.endsWith("release4k")) {
        throw RuntimeException("Invalid CWD! Execute this main class from 'kpotpourri' root directory, instead of submodule 'release4k' ;)")
    }

    val relativeKpotPath = "../github2/kpotpourri" // navigate to proper checkout location :)
    val liveKpotFolder = File(relativeKpotPath)
    val versionTxtFilename = "version.txt"
    val gitUrl = "https://github.com/christophpickl/kpotpourri.git"

    // =================================================================================================================

    val currentVersion = readVersionFromTxt("$relativeKpotPath/$versionTxtFilename").toVersion2()
    val nextVersion = readVersion2FromStdin(prompt = "Enter next release version", defaultVersion = currentVersion.incrementMinor())
    val nextVersionString = nextVersion.niceString
    val syspropNextVersion = "-Dkpotpourri.version=$nextVersionString"

    // =================================================================================================================
    printHeader("VERIFY NO CHANGES")
    execute("/usr/bin/git", "status", liveKpotFolder)
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
    printHeader("CHECKOUT GIT")
    checkoutGitProject(gitUrl)

    // =================================================================================================================
    printHeader("GRADLE BUILD")
    gradlew("clean check checkTodo test build $syspropNextVersion")

    // =================================================================================================================
    printHeader("CHANGE VERSION")
    File(gitCheckoutDirectory, versionTxtFilename).writeText(nextVersionString)

    git("status")
    git("add .")
    git("commit -m \"[Auto-Release] Preparing release $nextVersionString\"")
    git("tag $nextVersionString")

    // =================================================================================================================
    printHeader("BINTRAY UPLOAD")
    gradlew("bintrayUpload $syspropNextVersion")
    git("push")
    git("push origin --tags")


    execute("/usr/bin/git", "pull", liveKpotFolder)
    execute("/usr/bin/git", "fetch -p", liveKpotFolder)
}
