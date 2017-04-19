package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.numbers.format
import com.github.christophpickl.kpotpourri.github.GithubConfig
import com.github.christophpickl.kpotpourri.release4k.internal.Release4kImpl
import com.github.christophpickl.kpotpourri.release4k.internal.kout
import java.io.File
import kotlin.system.measureTimeMillis


inline fun release4k(func: Release4k.() -> Unit) {
    val msNeeded = measureTimeMillis {
        kout("Current working directory: ${File("").absolutePath}")
        val impl = Release4kImpl()
        func(impl)
        impl.onFinish()
    }
    kout("Total release time: ${(msNeeded / 1000.0).format(3)} secs")
}

interface Release4k {

    // could provide properties:
    // - project name
    // - git URL
    // - working directory (default: CWD/build/release4k)
    val release4kDirectory: File
    val gitCheckoutDirectory: File

    fun checkoutGitProject(gitUrl: String)
    fun initGithub(config: GithubConfig)
    fun readVersionFromTxt(relativeFilePath: String): Version
    fun writeVersionToTxt(relativeFilePath: String, version: Version)
    fun git(command: String)
    fun gradlew(command: String)
    fun printHeader(message: String)
    fun promptUser(prompt: String): String

    fun execute(cmd: String, args: String, cwd: File, suppressKout: Boolean = false)
}

open class Release4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)
