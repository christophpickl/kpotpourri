package com.github.christophpickl.kpotpourri.test4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.nullValue
import com.natpryce.hamkrest.assertion.assertThat
import com.nhaarman.mockito_kotlin.mock
import org.testng.ITestContext
import org.testng.ITestResult
import org.testng.SkipException
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class TestngTest {

    fun `skip throws`() {
        assertThrown<SkipException> {
            skip("any message")
        }
    }

    @DataProvider
    fun provideDataProviding(): Array<Array<out Any?>> = listOf<String?>(null).toDataProviding()

    @Test(dataProvider = "provideDataProviding")
    fun `toDataProviding - supportsNulls`(maybe: String?) {
        assertThat(maybe, nullValue())
    }

    fun `LogTestListener has got no side effects`() {
        // could test if everything was probably logged by adding a custom log appender
        LogTestListener().apply {
            val result = mock<ITestResult>()
            val context = mock<ITestContext>()
            onTestStart(result)
            onTestSuccess(result)
            onTestSkipped(result)
            onTestFailure(result)
            onStart(context)
            onFinish(context)
            onTestFailedButWithinSuccessPercentage(result)
        }
    }
}
