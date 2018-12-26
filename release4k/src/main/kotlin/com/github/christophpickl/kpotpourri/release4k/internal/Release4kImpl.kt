package com.github.christophpickl.kpotpourri.release4k.internal

import com.github.christophpickl.kpotpourri.common.file.resetDirectory
import com.github.christophpickl.kpotpourri.common.file.verifyExists
import com.github.christophpickl.kpotpourri.common.io.Keyboard
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.common.process.ProcessExecuter
import com.github.christophpickl.kpotpourri.common.process.ProcessExecuterImpl
import com.github.christophpickl.kpotpourri.github.GithubApi
import com.github.christophpickl.kpotpourri.github.RepositoryConfig
import com.github.christophpickl.kpotpourri.github.buildGithub4k
import com.github.christophpickl.kpotpourri.http4k.ServerConfig
import com.github.christophpickl.kpotpourri.release4k.Release4k
import com.github.christophpickl.kpotpourri.release4k.Release4kException
import com.github.christophpickl.kpotpourri.release4k.Version
import java.io.File

/**
 * Actual implementation.
 */
@Suppress("KDocMissingDocumentation")
/*pseudo-internal*/ class Release4kImpl(
        private val workingDirectory: File = File("."),
        private val process: ProcessExecuter = ProcessExecuterImpl()
) : Release4k, ProcessExecuter by process {

    private val log = LOG {}

    override val release4kDirectory = File(workingDirectory, "build/release4k").apply { resetDirectory() }
    override val gitCheckoutDirectory = File(release4kDirectory, "git_checkout")

    private var github: GithubApi? = null
    internal val _github: GithubApi? get() = github

    init {
        log.debug { "workingDirectory: ${workingDirectory.canonicalPath}" }
        log.debug { "release4kDirectory: ${release4kDirectory.canonicalPath}" }
        log.debug { "gitCheckoutDirectory: ${gitCheckoutDirectory.canonicalPath}" }
    }

    override fun initGithub(repository: RepositoryConfig, server: ServerConfig) {
        if (github != null) {
            throw Release4kException("initGithub() already invoked! ($github)")
        }
        github = buildGithub4k(repository, server)
    }

    override fun checkoutGitProject(gitUrl: String) {
        process.execute("/usr/bin/git", "clone $gitUrl ${gitCheckoutDirectory.name}", release4kDirectory)
    }

    override fun promptUser(prompt: String): String {
        println(prompt)
        println(">> ")
        return Keyboard.readLine()
    }

    override fun readVersionFromTxt(relativeFilePath: String): Version {
        val fileContent = File(relativeFilePath).apply {
            log.debug { "readVersionFromTxt(path=$absolutePath)" }
            verifyExists()
        }.readText()
        val version = VersionParser.parse(fileContent)
        kout("Read from [$relativeFilePath] version: ${version.niceString}")
        return version
    }

    override fun gradlew(command: String) {
        // MINOR or: process.execute(File(gitCheckoutDirectory, "gradlew").canonicalPath, command)
        process.execute("./gradlew", command, gitCheckoutDirectory)
    }

    override fun git(command: String) {
        process.execute("git", command, gitCheckoutDirectory)
    }

    /*pseudo-internal*/ fun onFinish() {
        try {
            // or maybe try to display (macOS) notification instead?
            process.execute("say", "\"Release build finished.\"", release4kDirectory, suppressOutput = true)
        } catch (e: Exception) {
            println("Release build finished.")
        }
    }

}
