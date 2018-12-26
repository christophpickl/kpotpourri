package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.process.ProcessExecuter
import com.github.christophpickl.kpotpourri.common.time.MsTimification
import com.github.christophpickl.kpotpourri.common.time.timify
import com.github.christophpickl.kpotpourri.github.GITHUB_COM
import com.github.christophpickl.kpotpourri.github.RepositoryConfig
import com.github.christophpickl.kpotpourri.http4k.ServerConfig
import com.github.christophpickl.kpotpourri.release4k.internal.Release4kImpl
import com.github.christophpickl.kpotpourri.release4k.internal.kout
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * Core entry function providing context to setup the release script.
 */
inline fun release4k(workingDirectory: File = File("."), func: Release4k.() -> Unit) {
    val msNeeded = measureTimeMillis {
        kout("Current working directory: ${File("./").canonicalPath}")
        kout("Configured working directory: ${workingDirectory.canonicalPath}")
        val impl = Release4kImpl(workingDirectory = workingDirectory)
        func(impl)
        impl.onFinish()
    }
    kout("Total release time: " + msNeeded.timify(MsTimification.MinutesAndSeconds))
}


/**
 * Core DSL setting configs and invoking misc shell commands.
 */
interface Release4k : ProcessExecuter {

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
    fun initGithub(repository: RepositoryConfig, server: ServerConfig = GITHUB_COM)

    /**
     * Reads the given file, expecting it to contain a valid version specification.
     */
    fun readVersionFromTxt(relativeFilePath: String): Version

    /**
     * Execute any valid GIT command.
     */
    fun git(args: List<String>)

    /**
     * Execute any valid gradle command.
     */
    fun gradlew(args: List<String>)

    /**
     * Ask the user for input.
     */
    fun promptUser(prompt: String): String

    /**
     * Prints a header line.
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
