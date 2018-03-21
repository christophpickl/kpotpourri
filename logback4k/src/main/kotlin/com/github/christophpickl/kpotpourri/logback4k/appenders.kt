package com.github.christophpickl.kpotpourri.logback4k

import ch.qos.logback.classic.Level
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender

/**
 * Write to console using logback's [ConsoleAppender] class.
 */
@Logback4kMarker
interface ConsoleAppenderBuilder {

    /** The name of the appender. */
    var appenderName: String

    /** Message pattern. */
    var pattern: String

    /** Log level. */
    var level: Level

}

/**
 * Write to a file using logback's [FileAppender] class.
 */
@Logback4kMarker
interface FileAppenderBuilder {

    /**
     * Path to target log file.
     *
     * E.g.: /var/log4k/myapplication.log
     */
    var file: String

    /**
     * Path to target log file.
     *
     * E.g.: /var/log4k/myapplication.%d{yyyy-MM-dd}.log
     */
    var filePattern: String

    /** The name of the appender. */
    var appenderName: String

    /** Message pattern. */
    var pattern: String

    /** Log level. */
    var level: Level

    /**
     * How many instances for [filePattern] are to be persisted.
     */
    var maxHistory: Int

}
