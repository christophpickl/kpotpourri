package com.github.christophpickl.kpotpourri.logback4k

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.logback4k.internal.InternalLogbackConfig
import com.github.christophpickl.kpotpourri.logback4k.internal.context
import org.slf4j.Logger

/**
 * Core API object to configure logback programmatically with a nice DSL.
 */
object Logback4k {

    fun reconfigure(withConfig: LogbackConfig.() -> Unit) { // vararg appenders: Appender<ILoggingEvent>
        // context.statusManager.add(InfoStatus("Setting up log configuration.", context))
        val rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME)
        rootLogger.detachAndStopAllAppenders()

        val config = InternalLogbackConfig()
        withConfig(config)

        rootLogger.level = config.rootLevel
        config.packageLevels.forEach { packageName, level -> context.changeLevel(packageName, level) }
        config.appenders.forEach(rootLogger::addAppender)
    }

    private fun LoggerContext.changeLevel(packageName: String, level: Level) {
        getLogger(packageName).level = level
    }
}

/**
 * Supported logback configuration options.
 */
@Logback4kMarker interface LogbackConfig {
    /** Change the global log level for all appenders and packages. */
    var rootLevel: Level

    /** Change the log level for specific package names*/
    fun packageLevel(level: Level, vararg packageNames: String)

    /** Change the log level for specific package names*/
    fun packageLevel(level: Level, packageNames: List<String>)

    /**
     * Enter nested DSL to create a new appender logging to std out.
     */
    fun addConsoleAppender(withBuilder: ConsoleAppenderBuilder.() -> Unit = {})

    /**
     * Enter nested DSL to create a new appender logging to a (rolling) file.
     */
    fun addFileAppender(withBuilder: FileAppenderBuilder.() -> Unit = {})
}

@DslMarker
internal annotation class Logback4kMarker

open class Logback4kException(
    message: String,
    cause: Exception? = null
) : KPotpourriException(message, cause)
