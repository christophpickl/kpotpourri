package com.github.christophpickl.kpotpourri.logback4k.internal

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import com.github.christophpickl.kpotpourri.logback4k.ConsoleAppenderBuilder
import com.github.christophpickl.kpotpourri.logback4k.FileAppenderBuilder
import com.github.christophpickl.kpotpourri.logback4k.Logback4kException
import java.util.concurrent.atomic.AtomicInteger

private val appenderCounter = AtomicInteger(1)

internal data class InternalConsoleAppenderBuilder(
    override var appenderName: String = "LogbackConsoleAppender_" + appenderCounter.getAndIncrement(),
    override var pattern: String = defaultPattern,
    override var level: Level = Level.ALL
) : ConsoleAppenderBuilder {

    internal fun build() = ConsoleAppender<ILoggingEvent>().also {
        it.context = context
        it.name = appenderName
        it.encoder = patternLayout(pattern)
        it.start()
        it.addFilter(ThresholdFilter(level))
    }
}

internal data class InternalFileAppenderBuilder(
    override var file: String? = null,
    override var filePattern: String? = null,
    override var appenderName: String = "LogbackFileAppender_" + appenderCounter.getAndIncrement(),
    override var pattern: String = defaultPattern,
    override var level: Level = Level.ALL,
    override var maxHistory: Int = 10
) : FileAppenderBuilder {

    internal fun build() = RollingFileAppender<ILoggingEvent>().also { appender ->
        appender.context = context
        appender.name = appenderName
        appender.encoder = patternLayout(pattern)

        // file specific
        // MINOR would be nice to have some kind of internal logger
        println("file: $file")
        println("filePattern: $filePattern")
        file ?: throw Logback4kException("The mandatory property '${FileAppenderBuilder::file.name}' was not set!")
        filePattern
            ?: throw Logback4kException("The mandatory property '${FileAppenderBuilder::filePattern.name}' was not set!")
        appender.file = file
        appender.isAppend = true
        appender.isImmediateFlush = true
        appender.rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>().also { policy ->
            policy.context = context
            policy.setParent(appender)
            policy.fileNamePattern = filePattern
            policy.maxHistory = maxHistory
            policy.start()
        }

        appender.start()
        appender.addFilter(ThresholdFilter(level))
    }
}

