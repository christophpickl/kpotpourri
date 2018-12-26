package com.github.christophpickl.kpotpourri.build

import com.github.christophpickl.kpotpourri.common.io.Keyboard
import com.github.christophpickl.kpotpourri.common.os.Notification
import com.github.christophpickl.kpotpourri.common.os.Notifier
import com.github.christophpickl.kpotpourri.common.os.TextToSpeech
import com.github.christophpickl.kpotpourri.common.process.ExecuteContext
import com.github.christophpickl.kpotpourri.common.process.ProcessExecuterImpl
import com.github.christophpickl.kpotpourri.common.process.StringCapture
import com.github.christophpickl.kpotpourri.common.string.times
import mu.KotlinLogging.logger
import java.io.File
import kotlin.reflect.full.companionObjectInstance

inline fun build4k(
    enableLogging: Boolean = false,
    action: Build4k.() -> Unit
) {
    initBuild4kLogging(enableLogging)
    try {
        action(Build4k())
    } catch (e: ExitException) {
        // ignore
    }
}

class Build4k {
    var title = "Build4k"

    private val log = logger {}
    private val notifier = Notifier()
    private val tts = TextToSpeech()

    fun execute(command: String, args: List<String>, context: ExecuteContext? = null): Int =
        ProcessExecuterImpl.execute(command, args, context)

    fun executeOrFail(command: String, args: List<String>, context: ExecuteContext? = null) {
        val returnCode = execute(command, args, context)

        if (returnCode != 0) {
            val commands = listOf(command).plus(args)
            val fullCommand = commands.joinToString(" ")
            fail(context?.failNotification ?: Notification(
                title = title,
                message = "Return code $returnCode for command: '$fullCommand'"
            ))
        }
    }

    inline fun <reified V : Version<V>> readFromFile(file: File): V {
        require(file.exists() && file.isFile && file.canRead()) { "File must be an existing, readable file: ${file.absolutePath}" }
        val fileContent = file.readText().trim()
        val versionClass = V::class
        @Suppress("UNCHECKED_CAST") val parser = (versionClass.companionObjectInstance as VersionParser<V>)
        return parser.parse(fileContent)
            ?: throw Build4kException("File content '$fileContent' does not match ${versionClass.simpleName}!")
    }

    fun confirm(prompt: String): Boolean =
        Keyboard.readConfirmation(prompt)

    private val headerLength = 100
    
    fun printHeader(title: String) {
        val signs: String
        val additional: String
        if (title.length < headerLength - 4) {
            val toFill = (headerLength - title.length) / 2
            signs = "=".times(toFill)
            additional = if (title.length % 2 == 1) "=" else ""
        } else {
            signs = "==="
            additional = ""
        }
        println()
        println("$signs> ${String.format("%s", title)} <$signs" + additional)
        println()
    }

    fun <V : Version<V>> chooseVersion(default: V): V {
        while (true) {
            val input = Keyboard.readWithDefault(
                prompt = "What will be the next version?",
                default = default.toString()
            )
            default.readOther(input)?.let {
                return it
            }
        }
    }

    fun promptUser(prompt: String): String {
        println(prompt)
        print(">> ")
        return Keyboard.readLine()
    }

    fun gradlew(vararg args: String, context: ExecuteContext? = null) {
        executeOrFail("./gradlew", args.toList(), context)
    }

    fun git(vararg args: String, context: ExecuteContext? = null) {
        executeOrFail("git", args.toList(), context)
    }

    fun requireGitTagExists(version: String) {
        val gitOutput = StringCapture()
        git("tag", context = ExecuteContext(captureOutput = gitOutput))
        val tags = gitOutput.captured.lines().map { it.trim() }.filter { it.isNotEmpty() }
        if (!tags.contains(version)) {
            println("Available tags: ${tags.joinToString()}")
            fail("Git tag '$version' does not exist!")
        }
    }

    fun environmentVariable(name: String): String {
        return System.getenv(name) ?: fail("Environment variable '$name' not defined!")
    }

    fun github(repoOwner: String, repoName: String, authToken: String, action: GitHub.() -> Unit) {
        action(GitHub(
            build4k = this,
            repoOwner = repoOwner,
            repoName = repoName,
            authToken = authToken
        ))
    }

    fun displayNotification(message: String) {
        displayAndPrint(Notification(title, message))
    }

    fun say(text: String) {
        tts.say(text)
    }

    fun whenFail(message: String) = ExecuteContext(
        failNotification = Notification(title, message)
    )

    fun fail(message: String): Nothing {
        fail(Notification(title, message))
    }

    private fun fail(notification: Notification): Nothing {
        log.error { "Fail! $notification" }
        displayAndPrint(notification)
        exit()
    }

    private fun displayAndPrint(notification: Notification) {
        notifier.display(notification)
        println("[NOTIFY] ${notification.title} - ${notification.message}")
    }

    fun exit(): Nothing {
        throw ExitException()
    }

}

class ExitException() : RuntimeException("Exit")

open class Build4kException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
