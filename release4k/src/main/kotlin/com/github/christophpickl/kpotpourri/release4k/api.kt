package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.time.MsTimification
import com.github.christophpickl.kpotpourri.common.time.timify
import com.github.christophpickl.kpotpourri.github.RepositoryConfig
import com.github.christophpickl.kpotpourri.release4k.internal.Release4kImpl
import com.github.christophpickl.kpotpourri.release4k.internal.kout
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * Core entry function providing context to setup the release script.
 */
inline fun release4k(func: Release4k.() -> Unit) {
    val msNeeded = measureTimeMillis {
        kout("Current working directory: ${File("./").canonicalPath}")
        val impl = Release4kImpl()
        func(impl)
        impl.onFinish()
    }
    kout("Total release time: " + msNeeded.timify(MsTimification.MinutesAndSeconds))
}


/**
 * Core DSL setting configs and invoking misc shell commands.
 */
interface Release4k {

    // could provide properties:
    // - project name
    // - git URL
    // - working directory (default: CWD/build/release4k)

    /**
     * Kind-a build directory.
     */
    val release4kDirectory: File

    /**
     * Access the checked out content from GitHub
     *
     * @see checkoutGitProject
     */
    val gitCheckoutDirectory: File

    /**
     * Checks out the given GitHub URL into the release4k build directory.
     *
     * @see gitCheckoutDirectory
     */
    fun checkoutGitProject(gitUrl: String)

    /**
     * Sets the internal github4k instance.
     *
     * @throws Release4kException if this method is invoked twice
     */
    fun initGithub(config: RepositoryConfig)

    /**
     * Reads the given file, expecting it to contain a valid version specification.
     */
    fun readVersionFromTxt(relativeFilePath: String): Version

    /**
     * Execute any valid GIT command.
     */
    fun git(command: String)

    /**
     * Execute any valid gradle command.
     */
    fun gradlew(command: String)

    /**
     * Ask the user for input.
     */
    fun promptUser(prompt: String): String

    /**
     * Rints a header line.
     */
    fun printHeader(message: String) {
        println()
        println("===> $message <===")
    }
}

/**
 * Specific exception.
 */
open class Release4kException(message: String, cause: Exception? = null) : KPotpourriException(message, cause)
