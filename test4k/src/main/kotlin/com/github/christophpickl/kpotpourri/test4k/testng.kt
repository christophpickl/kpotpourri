package com.github.christophpickl.kpotpourri.test4k

import mu.KotlinLogging
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestNGListener
import org.testng.ITestResult
import org.testng.SkipException

/**
 * Simplify skiping a TestNG test, as otherwise have to throw a custom exception which makes the code below unreachable generating a nasty warning.
 */
fun skip(message: String) {
    throw SkipException(message)
}

/**
 * Convert any list of objects to a proper array of arrays to be used for TestNG data providers.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> List<T>.toDataProviding() =
        map { arrayOf(it) }.toTypedArray() as Array<Array<out Any?>>


/**
 * Better to declare this listener as a default listener in IntelliJ's default configuration.
 */
@Suppress("unused", "KDocMissingDocumentation")
class LogTestListener : ITestNGListener, ITestListener {

    private val log = KotlinLogging.logger {}

    override fun onTestStart(result: ITestResult) {
        log.info(buildMessage("START", result))
    }

    override fun onTestSuccess(result: ITestResult) {
        log.info(buildMessage("SUCCESS", result))
    }

    override fun onTestSkipped(result: ITestResult) {
        log.warn(buildMessage("SKIP", result))
    }

    override fun onTestFailure(result: ITestResult) {
        log.info(buildMessage("FAIL", result))
    }

    override fun onStart(context: ITestContext) {
        log.info("Test Suite STARTED")
    }

    override fun onFinish(context: ITestContext) {
        log.info("Test Suite FINISHED")
    }

    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) {}

    private fun buildMessage(label: String, result: ITestResult) = {
        "======> $label - ${result.testClass?.realClass?.simpleName}#${result.method?.methodName}()"
    }

}
