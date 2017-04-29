package com.github.christophpickl.kpotpourri.logback4k.internal

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import com.github.christophpickl.kpotpourri.logback4k.ConsoleAppenderBuilder

internal data class InternalConsoleAppenderBuilder(
        override var appenderName: String = "someDefault", // TODO make proper default
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

// TODO implement rolling file appender (and test!)