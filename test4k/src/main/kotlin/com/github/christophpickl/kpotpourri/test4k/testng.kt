package com.github.christophpickl.kpotpourri.test4k

import mu.KotlinLogging
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestNGListener
import org.testng.ITestResult
import org.testng.SkipException

/**
 * Simplify skiping a TestNG test.
 */
fun skip(message: String) {
    throw SkipException(message)
}


// TODO remove that one from Gadsu, and enable as default TestNG listener in intellij (in gadsu and kpot)
class LogTestListener : ITestNGListener, ITestListener {

    private val log = KotlinLogging.logger {}

    override fun onTestStart(result: ITestResult) {
        logTest("START", result)
    }

    override fun onTestSuccess(result: ITestResult) {
        logTest("SUCCESS", result)
    }

    override fun onTestSkipped(result: ITestResult) {
        // copy and paste ;)
        log.warn("======> {} - {}#{}()", "SKIP", result.testClass.realClass.simpleName, result.method.methodName)
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

    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) {}

    private fun logTest(label: String, result: ITestResult) {
        log.info("======> {} - {}#{}()", label, result.testClass.realClass.simpleName, result.method.methodName)
    }

}
