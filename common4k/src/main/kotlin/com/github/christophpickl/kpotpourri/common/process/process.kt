package com.github.christophpickl.kpotpourri.common.process

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.os.Notification
import mu.KotlinLogging.logger
import java.io.File

/**
 * Execute any process like 'git' or 'gradle' like it's running in the terminal using Java's ProcessBuilder.
 */
interface ProcessExecuter {

    /**
     * @param command name of the process to start, e.g.: "git" or "./gradlew"
     * @param args the arguments for the process, e.g.: "co master"
     */
    fun execute(command: String, args: List<String>, context: ExecuteContext? = null): Int

    /**
     * @see execute but throws exception when return code is not equals 0.
     */
    fun executeOrThrow(command: String, args: List<String>, context: ExecuteContext? = null)
}

class StringCapture {
    var captured: String = ""
}

/**
 * @param cwd the current working directory.
 */
data class ExecuteContext(
    val cwd: File = File("."),
    val suppressOutput: Boolean = false,
    val failNotification: Notification? = null,
    val captureOutput: StringCapture? = null
)

/**
 * Implementation which should be used in production code.
 */
object ProcessExecuterImpl : ProcessExecuter {

    private val log = logger {}

    override fun execute(command: String, args: List<String>, context: ExecuteContext?): Int {
        val ctx = context ?: ExecuteContext()
        val commandList = listOf(command).plus(args)
        val commandString = commandList.joinToString(" ")
        log.info { "Executing command: $commandString" }
        if (!ctx.suppressOutput) {
            println("[${ctx.cwd.canonicalPath}] $commandString")
        }
        val builder = ProcessBuilder().apply {
            command(commandList)
            redirectInput(ProcessBuilder.Redirect.INHERIT)
            if (ctx.captureOutput == null) {
                redirectOutput(ProcessBuilder.Redirect.INHERIT)
            }
            redirectError(ProcessBuilder.Redirect.INHERIT)
//             inheritIO() // redirect IN/OUT/ERR to be same as of java process
            directory(ctx.cwd)
        }
        val process = builder.start()
        val exitCode = process.waitFor()
        if (ctx.captureOutput != null) {
            val processOutput = process.inputStream.bufferedReader().use { it.readText() }
            ctx.captureOutput.captured = processOutput
        }
        log.debug { "Command returned exit code: $exitCode" }
        return exitCode
    }

    override fun executeOrThrow(command: String, args: List<String>, context: ExecuteContext?) {
        val exitCode = execute(command, args, context)
        if (exitCode != 0) {
            throw KPotpourriException("Failed to execute '$command ${args.joinToString(" ")}' with exit code $exitCode! (context=$context)")
        }
    }

}
