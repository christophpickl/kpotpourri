/*
package com.github.christophpickl.kpotpourri.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.rolling.TriggeringPolicyBase
import ch.qos.logback.core.status.InfoStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestNGListener
import org.testng.ITestResult
import org.testng.annotations.BeforeSuite
import org.testng.annotations.Listeners
import org.testng.annotations.Test
import java.io.File


abstract class BaseLogConfigurator {

    protected val defaultPattern = "%-43(%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread]) [%-5level] %logger{42} - %msg%n"
    protected val context: LoggerContext
    private var yetConfigured = false

    init {
        context = LoggerFactory.getILoggerFactory() as LoggerContext
    }

    protected fun consoleAppender(name: String,
                                  pattern: String = defaultPattern,
                                  withAppender: ((ConsoleAppender<ILoggingEvent>) -> Unit)? = null): Appender<ILoggingEvent> {
        val appender = ConsoleAppender<ILoggingEvent>()
        appender.context = context
        appender.name = name
        appender.encoder = patternLayout(pattern)
        if (withAppender != null) {
            withAppender(appender)
        }
        appender.start()
        return appender
    }

    protected fun fileAppender(name: String, filename: String, filenamePattern: String): Appender<ILoggingEvent> {
        val appender = RollingFileAppender<ILoggingEvent>()
        appender.file = filename

        // http://logback.qos.ch/manual/appenders.html#TimeBasedRollingPolicy
        // http://www.programcreek.com/java-api-examples/index.php?api=ch.qos.logback.core.rolling.TimeBasedRollingPolicy
        val rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>()
        rollingPolicy.context = context
        rollingPolicy.setParent(appender)
        rollingPolicy.fileNamePattern = filenamePattern
        rollingPolicy.maxHistory = 14 // two weeks
        rollingPolicy.start()
        appender.rollingPolicy = rollingPolicy

        val triggeringPolicy = RollOncePerSessionTriggeringPolicy<ILoggingEvent>()
        triggeringPolicy.start()
        appender.triggeringPolicy = triggeringPolicy
        appender.isAppend = true
        appender.context = context
        appender.name = name
        appender.encoder = patternLayout()
        appender.start()
        return appender
    }

    // or: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    protected fun patternLayout(pattern: String = defaultPattern): PatternLayoutEncoder {
        val layout = PatternLayoutEncoder()
        layout.context = context
        layout.pattern = pattern
        layout.start()
        return layout
    }

    fun configureLog() {
        if (yetConfigured) {
            println("Log configurator '${javaClass.simpleName}' has been already activated. " +
                    "(Usually happens because of manually enabling log in tests and by testng suites)")
            return
        }
        yetConfigured = true

        val status = context.statusManager
        status.add(InfoStatus("Setting up log configuration.", context))

        val logger = context.getLogger(Logger.ROOT_LOGGER_NAME)
        logger.detachAndStopAllAppenders()

        configureInternal(logger)
    }

    abstract protected fun configureInternal(logger: ch.qos.logback.classic.Logger)

    protected fun changeLevel(packageName: String, level: ch.qos.logback.classic.Level) {
        context.getLogger(packageName).level = level
    }
}


// http://stackoverflow.com/questions/2492022/how-to-roll-the-log-file-on-startup-in-logback
private class RollOncePerSessionTriggeringPolicy<E> : TriggeringPolicyBase<E>() {
    private var doRolling: Boolean = true
    override fun isTriggeringEvent(activeFile: File, event: E): Boolean {
        // roll the first time when the event gets called
        if (doRolling) {
            doRolling = false
            return true
        }
        return false
    }
}

@Test
@Listeners(LogTestListener::class)
class TestLogger : BaseLogConfigurator() {

    override fun configureInternal(logger: ch.qos.logback.classic.Logger) {
        logger.level = Level.ALL

        arrayOf(
                "org.apache",
                "org.springframework",
                "org.flywaydb"
        ).forEach { changeLevel(it, Level.WARN) }
        logger.addAppender(consoleAppender("Gadsu-ConsoleAppender", "%d{HH:mm:ss} [%-5level] %logger{42} - %msg%n"))
    }

    @BeforeSuite
    fun initLogging() {
        configureLog()
    }

}

class LogTestListener :  ITestNGListener, ITestListener {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun onTestStart(result: ITestResult) {
        logTest("START", result)
    }

    override fun onTestSuccess(result: ITestResult) {
        logTest("SUCCESS", result)
    }

    override fun onTestSkipped(result: ITestResult) {
        // copy and paste ;)
        log.warn("======> {} - {}#{}", "SKIP", result.testClass.realClass.simpleName, result.method.methodName)
    }

    override fun onTestFailure(result: ITestResult) {
        logTest("FAIL", result)
    }


    override fun onStart(context: ITestContext) {
        log.info("Test Suite STARTED")
    }

    override fun onFinish(context: ITestContext) {
        log.info("Test Suite FINISHED")
    }

    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) { }

    private fun logTest(label: String, result: ITestResult) {
        log.info("======> {} - {}#{}", label, result.testClass.realClass.simpleName, result.method.methodName)
    }

}

class LogTestEnablerListener :  ITestNGListener, ITestListener {
    override fun onStart(context: ITestContext) {
        TestLogger().configureLog()
    }
    override fun onFinish(context: ITestContext) { }
    override fun onTestStart(result: ITestResult) { }
    override fun onTestSuccess(result: ITestResult) { }
    override fun onTestSkipped(result: ITestResult) { }
    override fun onTestFailure(result: ITestResult) { }
    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) { }

}

*/
