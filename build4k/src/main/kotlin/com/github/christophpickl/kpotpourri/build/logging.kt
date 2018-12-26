package com.github.christophpickl.kpotpourri.build

import ch.qos.logback.classic.Level
import com.github.christophpickl.kpotpourri.logback4k.Logback4k

fun initBuild4kLogging(enableLogging: Boolean) {
    Logback4k.reconfigure {
        if (enableLogging) {
            rootLevel = Level.ALL
//            packageLevel(Level.WARN, "org.apache", "org.jetty")
            addConsoleAppender {
                appenderName = "Build4kAppender"
                level = Level.ALL // by default
                pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] %logger{42} - %msg%n"
            }
        }
    }
}
