package com.github.christophpickl.kpotpourri.release4k.internal

import com.github.christophpickl.kpotpourri.common.file.resetDirectory
import com.github.christophpickl.kpotpourri.common.file.verifyExists
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.common.string.splitAsArguments
import com.github.christophpickl.kpotpourri.github.GithubApi
import com.github.christophpickl.kpotpourri.github.GithubConfig
import com.github.christophpickl.kpotpourri.github.buildGithub4k
import com.github.christophpickl.kpotpourri.release4k.Release4k
import com.github.christophpickl.kpotpourri.release4k.Release4kException
import com.github.christophpickl.kpotpourri.release4k.Version
import java.io.File


internal class Release4kImpl : Release4k {
    private val log = LOG {}

    override val release4kDirectory = File("build/release4k")
    override val gitCheckoutDirectory = File(release4kDirectory, "git_checkout")

    private var github: GithubApi? = null

    override fun initGithub(config: GithubConfig) {
        if (github != null) {
            throw Release4kException("initGithub() already invoked! ($github)")
        }
        github = buildGithub4k(config)
    }

    override fun checkoutGitProject(gitUrl: String) {
        gitCheckoutDirectory.resetDirectory()
        execute("git", "clone $gitUrl ${gitCheckoutDirectory.name}", release4kDirectory)
    }


    override fun promptUser(prompt: String): String {
        println(prompt)
        println(">> ")
        return readLine()!!
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

    private fun execute(cmd: String, args: String, cwd: File, suppressKout: Boolean = false) {
        if (!suppressKout) {
            koutCmd(cwd, "$cmd $args")
        }
        val cmdAndArgs = buildCmdAndArgs(cmd, args)
        val processBuilder = ProcessBuilder(cmdAndArgs)
        processBuilder.inheritIO()
        processBuilder.directory(cwd)
        val process = processBuilder.start()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw Release4kException("Failed to execute '$cmd $args' with exit code $exitCode!")
        }
    }

    private fun buildCmdAndArgs(cmd: String, args: String): List<String> {
        return ArrayList<String>().apply {
            add(cmd)
            addAll(args.splitAsArguments())
        }
    }

    override fun gradlew(command: String) {
//        execute(File(gitCheckoutDirectory, "gradlew").canonicalPath, command)
        execute("gradlew", command, gitCheckoutDirectory)
    }

    override fun git(command: String) {
        execute("git", command, gitCheckoutDirectory)
    }

    fun execute() {
        // TODO actually just create a descriptive task and add it to queue
        log.debug("execute()")

        execute("say", "\"Release build finished.\"", release4kDirectory, suppressKout = true)
    }

}
