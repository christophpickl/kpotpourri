package com.github.christophpickl.kpotpourri.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

interface MyLoggerFactory {
    fun getLogger(clazz: Class<*>): Logger
}

// in order to work, this system property must NOT be enabled: "slf4j.detectLoggerNameMismatch"
object DefaultLoggerFactory : MyLoggerFactory {
    override fun getLogger(clazz: Class<*>) = LoggerFactory.getLogger(clazz)!!
}

fun Level.invokeLog(targetLog: Logger, message: String) {
    when (this) {
        Level.ERROR -> targetLog.error(message)
        Level.WARN -> targetLog.warn(message)
        Level.INFO -> targetLog.info(message)
        Level.DEBUG -> targetLog.debug(message)
        Level.TRACE -> targetLog.trace(message)
    }
}

fun Logger.isEnabled(level: Level): Boolean = when (level) {
    Level.ERROR -> isErrorEnabled
    Level.WARN -> isWarnEnabled
    Level.INFO -> isInfoEnabled
    Level.DEBUG -> isDebugEnabled
    Level.TRACE -> isTraceEnabled
}
