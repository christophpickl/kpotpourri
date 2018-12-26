package com.github.christophpickl.kpotpourri.bootaop

// FIXME enable integration test (requires spring boot startup + mockito and stuff)
/*
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [LogAspectTestConfig::class])
class LogAspectIntegrationTest {

    @Inject
    private lateinit var loggedComponent: LoggedComponent

    @MockBean
    private lateinit var loggerFactory: MyLoggerFactory

    @Test
    fun `Given log level enabled When invoke logged method Then proper debug messaged logged`() {
        val logger = initLogger(LoggedComponent::class.java, isDebugEnabled = true)

        loggedComponent.loggedMethod()

        assertThat(logger.captureDebug(), equalTo("loggedMethod()"))
    }

    @Test
    fun `Given log level enabled When invoke logged method with parameter Then proper debug messaged logged`() {
        val logger = initLogger(LoggedComponent::class.java, isDebugEnabled = true)

        loggedComponent.loggedMethodWithParam("paramValue")

        assertThat(logger.captureDebug(), equalTo("loggedMethodWithParam(paramName=paramValue)"))
    }

    @Test
    fun `Given log level enabled When invoke logged method with two parameters Then proper debug messaged logged`() {
        val logger = initLogger(LoggedComponent::class.java, isDebugEnabled = true)

        loggedComponent.loggedMethodWithParams("paramValue", 42)

        assertThat(logger.captureDebug(), equalTo("loggedMethodWithParams(paramName1=paramValue, paramName2=42)"))
    }

    @Test
    fun `Given log level disabled When invoke logged method Then nothing logged`() {
        val logger = initLogger(LoggedComponent::class.java, isDebugEnabled = false)

        loggedComponent.loggedMethod()

        verify(logger).isDebugEnabled
        verifyNoMoreInteractions(logger)
    }

    @Test
    fun `Given log level enabed When invoke method with NoLog param Then dont log param value`() {
        val logger = initLogger(LoggedComponent::class.java, isDebugEnabled = true)

        loggedComponent.loggedMethodNoLogParam("notPrinted")

        assertThat(logger.captureDebug(), equalTo("loggedMethodNoLogParam(paramName)"))
    }


    @Test
    fun `Given log level enabed When invoke method with NoLog collection param Then only log params value size`() {
        val logger = initLogger(LoggedComponent::class.java, isDebugEnabled = true)

        loggedComponent.loggedMethodNoLogCollectionParam(listOf("notPrinted"))

        assertThat(logger.captureDebug(), equalTo("loggedMethodNoLogCollectionParam(paramName.size=1)"))
    }

    @Test
    fun `Given log level enabled When invoke method with custom log level Then log`() {
        val logger = initLogger(LoggedComponent::class.java, isTraceEnabled = true)

        loggedComponent.loggedMethodTraceLevel()

        assertThat(logger.captureTrace(), equalTo("loggedMethodTraceLevel()"))
        verify(logger).isTraceEnabled
        verifyNoMoreInteractions(logger)
    }

    @Test
    fun `Given log log level disabled WHen invoke method with custom log level Then`() {
        val logger = initLogger(LoggedComponent::class.java, isTraceEnabled = false)

        loggedComponent.loggedMethodTraceLevel()

        verify(logger).isTraceEnabled
        verifyNoMoreInteractions(logger)
    }

    private fun initLogger(logTarget: Class<*>, isDebugEnabled: Boolean? = null, isTraceEnabled: Boolean? = null): Logger {
        val logger = mock<Logger>()
        whenever(loggerFactory.getLogger(logTarget)).thenReturn(logger)
        if (isDebugEnabled != null) {
            whenever(logger.isDebugEnabled).thenReturn(isDebugEnabled)
        }
        if (isTraceEnabled != null) {
            whenever(logger.isTraceEnabled).thenReturn(isTraceEnabled)
        }
        return logger
    }

    private fun Logger.captureDebug(): String {
        val captor = argumentCaptor<String>()
        verify(this).debug(captor.capture())
        return captor.firstValue
    }

    private fun Logger.captureTrace(): String {
        val captor = argumentCaptor<String>()
        verify(this).trace(captor.capture())
        return captor.firstValue
    }

}

@TestConfiguration
@EnableAspectJAutoProxy
private class LogAspectTestConfig {

    @Bean
    fun loggedComponent() = LoggedComponent()

    @Bean
    fun logAspect(
        // will be wired by test
        @Suppress("SpringJavaInjectionPointsAutowiringInspection") loggerFactory: MyLoggerFactory
    ) = LogAspect(loggerFactory)

}

@Component
private class LoggedComponent {

    @Logged
    fun loggedMethod() {
    }

    @Logged
    fun loggedMethodWithParam(paramName: String) {
    }

    @Logged
    fun loggedMethodWithParams(paramName1: String, paramName2: Int) {
    }

    @Logged
    fun loggedMethodNoLogParam(@NoLog paramName: String) {
    }

    @Logged
    fun loggedMethodNoLogCollectionParam(@NoLog paramName: List<String>) {
    }

    @Logged(level = Level.TRACE)
    fun loggedMethodTraceLevel() {
    }

}

*/
