package com.github.christophpickl.kpotpourri.logback4k

import ch.qos.logback.classic.Level
import ch.qos.logback.core.ConsoleAppender

/**
 * Write to console using logback's [ConsoleAppender] class.
 */
interface ConsoleAppenderBuilder {

    /** The name of the appender. */
    var appenderName: String

    /** Message pattern. */
    var pattern: String

    /** Log level. */
    var level: Level

}
