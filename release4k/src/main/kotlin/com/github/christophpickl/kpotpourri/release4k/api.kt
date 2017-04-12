package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.numbers.format
import com.github.christophpickl.kpotpourri.github.GithubConfig
import com.github.christophpickl.kpotpourri.release4k.internal.Release4kImpl
import com.github.christophpickl.kpotpourri.release4k.internal.kout
import java.io.File
import kotlin.system.measureTimeMillis

fun release4k(func: Release4k.() -> Unit) {
    val msNeeded = measureTimeMillis {
        kout("Current working directory: ${File("").absolutePath}")
        val impl = Release4kImpl()
        func(impl)
        impl.execute()
    }
    kout("Total release time: ${(msNeeded / 1000.0).format(3)} secs")
}

interface Release4k {
    var baseDirectory: File?

    fun initGithub(config: GithubConfig)
    fun readVersionFromTxt(relativeFilePath: String): Version
    fun git(command: String)
    fun gradlew(command: String)

    fun promptUser(prompt: String): String
}

open class Release4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)
