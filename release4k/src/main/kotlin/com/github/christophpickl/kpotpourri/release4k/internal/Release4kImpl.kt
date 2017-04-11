package com.github.christophpickl.kpotpourri.release4k.internal

import com.github.christophpickl.kpotpourri.common.file.verifyExists
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.github.GithubApi
import com.github.christophpickl.kpotpourri.github.GithubConfig
import com.github.christophpickl.kpotpourri.github.buildGithub4k
import com.github.christophpickl.kpotpourri.release4k.Release4k
import com.github.christophpickl.kpotpourri.release4k.Release4kException
import com.github.christophpickl.kpotpourri.release4k.Version
import java.io.File

internal class Release4kImpl : Release4k {
    private val log = LOG {}

    override var baseDirectory: File? = null
        get() = field
        set(value) {
            kout("Setting base directory to: ${value?.canonicalPath}")
            field = value
        }

    private var github: GithubApi? = null
    override fun initGithub(config: GithubConfig) {
        if (github != null) {
            throw Release4kException("initGithub() already invoked! ($github)")
        }
        github = buildGithub4k(config)
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
        val version = VersionReader.read(fileContent)
        kout("Read from [$relativeFilePath] version: ${version.niceString}")
        return version
    }

    private fun execute(cmd: String, args: String, suppressKout: Boolean = false) {
        if (!suppressKout) {
            koutCmd("$cmd $args")
        }
        val processBuilder = ProcessBuilder(cmd, args)
        processBuilder.inheritIO()
        baseDirectory?.let { processBuilder.directory(it) }
        val process = processBuilder.start()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw Release4kException("Failed to execute '$cmd $args' with exit code $exitCode!")
        }
    }

    override fun gradlew(command: String) {
        execute(File(baseDirectory, "gradlew").canonicalPath, command)
    }

    override fun git(command: String) {
        execute("git", command)
    }

    fun execute() {
        // TODO actually just create a descriptive task and add it to queue
        log.debug("execute()")

        execute("say", "Release build finished.", suppressKout = true)
    }

}
