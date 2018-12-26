package com.github.christophpickl.kpotpourri.bootaop

import com.github.christophpickl.kpotpourri.common.logging.DefaultLoggerFactory
import com.github.christophpickl.kpotpourri.common.logging.MyLoggerFactory
import com.github.christophpickl.kpotpourri.common.logging.invokeLog
import com.github.christophpickl.kpotpourri.common.logging.isEnabled
import mu.KotlinLogging.logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.event.Level

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Logged(
    val level: Level = Level.DEBUG
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NoLog

@Aspect
class LogAspect(
    private val logFactory: MyLoggerFactory = DefaultLoggerFactory
) {

    private val log = logger {}

    @Before("@annotation(com.github.christophpickl.kpotpourri.bootaop.Logged)")
    fun debugLogMessage(joinPoint: JoinPoint) {
        val targetLog = logFactory.getLogger(joinPoint.target::class.java)

        val signature = joinPoint.signature as MethodSignature
        val loggedAnnotation = signature.method.getAnnotation(Logged::class.java)
        if (!targetLog.isEnabled(loggedAnnotation.level)) {
            return
        }

        val methodName = signature.name
        val params = formatParameters(joinPoint, signature)
        loggedAnnotation.level.invokeLog(targetLog = targetLog, message = "$methodName($params)")
    }

    private fun formatParameters(joinPoint: JoinPoint, signature: MethodSignature): String {
        val parameterNames = signature.parameterNames

        if (parameterNames == null) {
            log.warn { "Parameter names not available. Please turn it on in gradle by adding 'javaParameters = true'." }
            return "???"
        }

        return parameterNames.mapIndexed { i, name ->
            "$name${formatParameter(joinPoint, signature, i)}"
        }.joinToString()
    }

    private fun formatParameter(joinPoint: JoinPoint, signature: MethodSignature, index: Int): String {
        val arg = joinPoint.args[index]
        val hasNoLogAnnotation = signature.method.parameterAnnotations[index].any { it is NoLog }
        if (!hasNoLogAnnotation) return "=$arg"
        if (arg is Collection<*>) return ".size=${arg.size}"
        return ""
    }

}
