package com.github.christophpickl.kpotpourri.logback4k

import ch.qos.logback.classic.Level
import com.github.christophpickl.kpotpourri.common.io.Io
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.not
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.google.common.io.Files
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import mu.KLogger
import mu.KotlinLogging
import org.testng.annotations.Test
import java.io.File
import java.nio.charset.Charset
import java.util.Date

@Test
class FileAppenderIntegrationTest {

    fun `success`() {
        val filePrefix = File(System.getProperty("java.io.tmpdir"), "logback4k-${Date().time}").canonicalPath
        val fileFile = File("$filePrefix.log")
        Logback4k.reconfigure {
            addFileAppender(file = fileFile.canonicalPath, filePattern = "$filePrefix-%d{yyyy-MM-dd}.log") {
                level = Level.ALL
            }
        }
        val log = LOG {}
        log.info { "anyLogMessage" }

        assertThat(fileFile.exists(), equalTo(true))
        val lines = Files.readLines(fileFile, Charset.defaultCharset())
        assertThat(lines, not(isEmpty))
    }

}

@Test
class ConsoleAppenderIntegrationTest {

    private val simplePattern = "[%level] %msg%n"

    fun `sunshine`() {
        Logback4k.reconfigure {
            addTestConsoleAppender(Level.ALL)
        }

        executeLog { info { "logInfo" } } shouldMatchValue "[INFO] logInfo\n"
    }

    fun `root level to warn, appender to all, Should not be logged`() {
        Logback4k.reconfigure {
            rootLevel = Level.WARN
            addTestConsoleAppender(Level.ALL)
        }

        executeLog { info { "logInfo" } } shouldMatchValue ""
    }

    fun `root level to WARN and appender to ALL and package to INFO, Should be logged`() {
        Logback4k.reconfigure {
            rootLevel = Level.WARN
            addTestConsoleAppender(Level.ALL)
            packageLevel(Level.INFO, "packagePrecedence")
        }

        loggerAndAssert("packagePrecedence", { info { "overrides root level" } }, "[INFO] overrides root level\n")

    }

    fun `package limitted`() {
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
