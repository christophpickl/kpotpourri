package com.github.christophpickl.kpotpourri.common.process

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import mu.KotlinLogging.logger
import java.io.File

/**
 * Execute any process like 'git' or 'gradle' like it's running in the terminal using Java's ProcessBuilder.
 */
interface ProcessExecuter {

    /**
     * @param command name of the process to start, e.g.: "git" or "./gradlew"
     * @param args the arguments for the process, e.g.: "co master"
     * @param cwd the current working directory.
     */
    fun execute(command: String, args: List<String>, cwd: File, suppressOutput: Boolean = false): Int

    /**
     * @see execute but throws exception when return code is not equals 0.
     */
    fun executeOrThrow(command: String, args: List<String>, cwd: File, suppressOutput: Boolean = false)
}

/**
 * Implementation which should be used in production code.
 */
object ProcessExecuterImpl : ProcessExecuter {

    private val log = logger {}

    override fun execute(command: String, args: List<String>, cwd: File, suppressOutput: Boolean): Int {
        val commandList = listOf(command).plus(args)
        val commandString = commandList.joinToString(" ")
        log.info { "Executing command: $commandString" }
        if (!suppressOutput) {
            println("[${cwd.canonicalPath}] $commandString")
        }
        val builder = ProcessBuilder().apply {
            command(commandList)
            inheritIO() // redirect IN/OUT/ERR to be same as of java process
            directory(cwd)
        }
        val process = builder.start()
        val exitCode = process.waitFor()
        log.debug { "Command returned exit code: $exitCode" }
        return exitCode
    }

    override fun executeOrThrow(command: String, args: List<String>, cwd: File, suppressOutput: Boolean) {
        val exitCode = execute(command, args, cwd, suppressOutput)
        if (exitCode != 0) {
            throw KPotpourriException("Failed to execute '$command ${args.joinToString(" ")}' with exit code $exitCode! (CWD: ${cwd.canonicalPath})")
        }
    }

}
