package com.github.christophpickl.kpotpourri.logback4k.internal

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import com.github.christophpickl.kpotpourri.logback4k.ConsoleAppenderBuilder
import com.github.christophpickl.kpotpourri.logback4k.LogbackConfig
import org.slf4j.LoggerFactory

internal val context: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext

internal data class InternalLogbackConfig(
        override var rootLevel: Level = Level.ALL
) : LogbackConfig {

    internal val packageLevels = mutableMapOf<String, Level>()
    internal val appenders = mutableListOf<Appender<ILoggingEvent>>()

    @Suppress("KDocMissingDocumentation")
    override fun packageLevel(level: Level, vararg packageNames: String) {
        packageNames.forEach { packageLevels += it to level }
    }

    @Suppress("KDocMissingDocumentation")
    override fun packageLevel(level: Level, packageNames: List<String>) {
        packageNames.forEach { packageLevels += it to level }
    }

    @Suppress("KDocMissingDocumentation")
    // or advanced via directly: withAppender: ((ConsoleAppender<ILoggingEvent>) -> Unit) = {}
    override fun addConsoleAppender(withBuilder: ConsoleAppenderBuilder.() -> Unit) {
        appenders += InternalConsoleAppenderBuilder().let { builder ->
            withBuilder(builder)
            builder.build()
        }
    }
}

internal class ThresholdFilter(private val level: Level) : Filter<ILoggingEvent>() {
    init {
        start()
    }

    /** Strangely has to be implemented yourself... */
    override fun decide(event: ILoggingEvent): FilterReply {
        if (event.level.isGreaterOrEqual(level)) {
            return FilterReply.ACCEPT
        }
        return FilterReply.DENY
    }

}
