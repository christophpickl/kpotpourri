package com.github.christophpickl.kpotpourri.logback4k

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.github.christophpickl.kpotpourri.logback4k.internal.InternalLogbackConfig
import com.github.christophpickl.kpotpourri.logback4k.internal.context
import org.slf4j.Logger

interface LogbackConfig {
    var rootLevel: Level

    fun packageLevel(level: Level, packageNames: List<String>)
    fun addConsoleAppender(withBuilder: ConsoleAppenderBuilder.() -> Unit = {})
}

interface ConsoleAppenderBuilder {
    var appenderName: String
    var pattern: String
    var level: Level
}

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
