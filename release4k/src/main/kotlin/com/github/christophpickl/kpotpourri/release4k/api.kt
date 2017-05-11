package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.time.MsTimification
import com.github.christophpickl.kpotpourri.common.time.timify
import com.github.christophpickl.kpotpourri.github.RepositoryConfig
import com.github.christophpickl.kpotpourri.release4k.internal.Release4kImpl
import com.github.christophpickl.kpotpourri.release4k.internal.kout
import java.io.File
import kotlin.system.measureTimeMillis


inline fun release4k(func: Release4k.() -> Unit) {
    val msNeeded = measureTimeMillis {
        kout("Current working directory: ${File("./").canonicalPath}")
        val impl = Release4kImpl()
        func(impl)
        impl.onFinish()
    }
    kout("Total release time: " + msNeeded.timify(MsTimification.MinutesAndSeconds))
}

interface Release4k {

    // could provide properties:
    // - project name
    // - git URL
    // - working directory (default: CWD/build/release4k)

    val release4kDirectory: File
    val gitCheckoutDirectory: File

    fun checkoutGitProject(gitUrl: String)
    fun initGithub(config: RepositoryConfig)
    fun readVersionFromTxt(relativeFilePath: String): Version
    fun git(command: String)
    fun gradlew(command: String)
    fun printHeader(message: String) {
        println()
        println("===> $message <===")
    }
    fun promptUser(prompt: String): String

    fun execute(cmd: String, args: String, cwd: File, suppressKout: Boolean = false)
}

open class Release4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)
