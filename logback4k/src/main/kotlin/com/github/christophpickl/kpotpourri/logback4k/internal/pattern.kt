package com.github.christophpickl.kpotpourri.logback4k.internal

import ch.qos.logback.classic.encoder.PatternLayoutEncoder

internal val defaultPattern = "%-43(%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread]) [%-5level] %logger{42} - %msg%n"

internal fun patternLayout(pattern: String = defaultPattern): PatternLayoutEncoder {
    val layout = PatternLayoutEncoder()
    layout.context = context
    layout.pattern = pattern
    layout.start()
    return layout
}
