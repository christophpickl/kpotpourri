package com.github.christophpickl.kpotpourri.logback4k

import ch.qos.logback.classic.Level
import com.github.christophpickl.kpotpourri.common.string.times
import mu.KLogger
import mu.KotlinLogging

fun main(args: Array<String>) {
    Logback4k.reconfigure {
        rootLevel = Level.ALL
        packageLevel(Level.WARN, listOf("pkgOnlyWarn"))
        addConsoleAppender {
            appenderName = "console1"
            level = Level.INFO
        }
        addConsoleAppender {
            appenderName = "console2"
            pattern = "console2${" ".times(36)}[%-5level] %logger{42} - %msg%n"
            level = Level.ALL
        }
    }

    listOf(
            "pkgOnlyWarn", "pkgRoot"
    ).forEach { KotlinLogging.logger(it).emitAllLevels() }
    /*
    2017-04-29 00:24:19.943 [main]              [WARN ] pkgOnlyWarn - KPOT warn
    console2                                    [WARN ] pkgOnlyWarn - KPOT warn
    2017-04-29 00:24:19.954 [main]              [ERROR] pkgOnlyWarn - KPOT error
    console2                                    [ERROR] pkgOnlyWarn - KPOT error
    console2                                    [TRACE] pkgRoot - KPOT trace
    console2                                    [DEBUG] pkgRoot - KPOT debug
    2017-04-29 00:24:19.954 [main]              [INFO ] pkgRoot - KPOT info
    console2                                    [INFO ] pkgRoot - KPOT info
    2017-04-29 00:24:19.955 [main]              [WARN ] pkgRoot - KPOT warn
    console2                                    [WARN ] pkgRoot - KPOT warn
    2017-04-29 00:24:19.955 [main]              [ERROR] pkgRoot - KPOT error
    console2                                    [ERROR] pkgRoot - KPOT error
     */
}

fun KLogger.emitAllLevels() {
    trace("KPOT trace")
    debug("KPOT debug")
    info("KPOT info")
    warn("KPOT warn")
    error("KPOT error")
}
