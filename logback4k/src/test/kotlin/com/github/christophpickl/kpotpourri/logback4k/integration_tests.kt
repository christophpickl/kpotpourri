package com.github.christophpickl.kpotpourri.logback4k

import ch.qos.logback.classic.Level
import com.github.christophpickl.kpotpourri.common.io.Io
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import mu.KLogger
import mu.KotlinLogging
import org.testng.annotations.Test

@Test class Logback4kIntegrationTest {

    private val simplePattern = "[%level] %msg%n"

    fun `ConsoleAppender - sunshine`() {
        Logback4k.reconfigure {
            addTestConsoleAppender(Level.ALL)
        }

        executeLog { info { "logInfo" } } shouldMatchValue "[INFO] logInfo\n"
    }

    fun `ConsoleAppender - root level to warn, appender to all, Should not be logged`() {
        Logback4k.reconfigure {
            rootLevel = Level.WARN
            addTestConsoleAppender(Level.ALL)
        }

        executeLog { info { "logInfo" } } shouldMatchValue ""
    }

    fun `ConsoleAppender - root level to WARN and appender to ALL and package to INFO, Should be logged`() {
        Logback4k.reconfigure {
            rootLevel = Level.WARN
            addTestConsoleAppender(Level.ALL)
            packageLevel(Level.INFO, "packagePrecedence")
        }

        loggerAndAssert("packagePrecedence", { info { "overrides root level" } }, "[INFO] overrides root level\n")

    }

    fun `ConsoleAppender - package limitted`() {
        Logback4k.reconfigure {
            addTestConsoleAppender(Level.ALL)
            packageLevel(Level.WARN, "pkgLimit")
        }

        loggerAndAssert("pkgLimit", { info { "will not be displayed as INFO < WARN" } }, "")
        loggerAndAssert("pkgLimit", { error { "pkgLimitError" } }, "[ERROR] pkgLimitError\n")
    }

    private fun LogbackConfig.addTestConsoleAppender(appenderLevel: Level) {
        addConsoleAppender {
            appenderName = "testConsoleAppender"
            level = appenderLevel
            pattern = simplePattern
        }
    }

    private fun loggerAndAssert(packageName: String, withLogger: KLogger.() -> Unit, expectedStdout: String) {
        Io.readFromStdOut {
            withLogger(KotlinLogging.logger(packageName))
        } shouldMatchValue expectedStdout
    }

    private fun executeLog(func: KLogger.() -> Unit): String {
        val log = LOG {}
        return Io.readFromStdOut {
            func(log)
        }
    }

}
