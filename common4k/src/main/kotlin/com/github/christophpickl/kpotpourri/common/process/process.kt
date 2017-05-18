package com.github.christophpickl.kpotpourri.common.process

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.string.splitAsArguments
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
    fun execute(command: String, args: String, cwd: File, suppressOutput: Boolean = false)
}

/**
 * Implementation which should be used in production code.
 */
class ProcessExecuterImpl : ProcessExecuter {

    override fun execute(command: String, args: String, cwd: File, suppressOutput: Boolean) {
        if (!suppressOutput) {
            println("[${cwd.canonicalPath}] $command $args")
        }
        val cmdAndArgs = buildCmdAndArgs(command, args)
        val processBuilder = ProcessBuilder(cmdAndArgs)
        processBuilder.inheritIO()
        processBuilder.directory(cwd)
        val process = processBuilder.start()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw KPotpourriException("Failed to execute '$command $args' with exit code $exitCode!")
        }
    }

    private fun buildCmdAndArgs(cmd: String, args: String): List<String> =
            ArrayList<String>().apply {
                add(cmd)
                addAll(args.splitAsArguments())

            }

}
