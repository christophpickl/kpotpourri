package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import mu.KotlinLogging.logger
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaType

// MINOR create reflection4k
internal object TestInitializer {

    private val log = logger {}

    internal fun injectPort(testInstance: Any, port: Int) {
        testInstance::class.memberProperties
                .filter { it.findAnnotation<InjectMockPort>() != null }.forEach { prop ->
            prop.apply {
                ensureReturnType(Integer::class.java)
                ensureVisibility(KVisibility.PRIVATE)
                ensureLateInit()
                asMutable {
                    log.debug { "Setting test property @InjectPort '${testInstance::class.simpleName}.${prop.name}' to: $port" }
                    setRetainOldValue(testInstance, port)
                }
            }
        }
    }

    // could also allow to inject a complex object instead of a simple string representing the full base URL
    internal fun injectMockUrl(testInstance: Any, url: String) {
        testInstance::class.memberProperties
                .filter { it.findAnnotation<InjectMockUrl>() != null }.forEach { prop ->
            prop.apply {
                ensureReturnType(String::class.java)
                ensureVisibility(KVisibility.PRIVATE)
                ensureLateInit()
                asMutable {
                    log.debug { "Setting test property @InjectMockUrl '${testInstance::class.simpleName}.${prop.name}' to: $url" }
                    setRetainOldValue(testInstance, url)
                }
            }
        }
    }

    internal fun exjectOverridePort(testInstance: Any): Int? =
            testInstance::class.findAnnotation<OverrideMockPort>()?.port

    private fun <T> KProperty1<T, *>.ensureReturnType(expectedType: Class<*>) {
        if (returnType.javaType != expectedType) {
            throw TestInitializationException("Expected property '$name' to be of type ${expectedType.name}, but was: ${returnType.javaType.typeName}")
        }
    }

    private fun KProperty1<*, *>.ensureVisibility(expectedVisibility: KVisibility) {
        if (visibility != expectedVisibility) {
            throw TestInitializationException("Expected property '$name' to be $expectedVisibility, but was: $visibility")
        }
    }

    private fun KProperty1<*, *>.ensureLateInit() {
        if (!isLateinit) {
            throw TestInitializationException("Expected property '$name' to be marked as lateinit.")
        }
    }

    private inline fun <T, R> KProperty1<T, R>.asMutable(withMutabe: KMutableProperty<R>.() -> Unit) {
        @Suppress("UNCHECKED_CAST")
        withMutabe(this as? KMutableProperty<R> ?: throw TestInitializationException("Expected property '$name' to be mutable."))
    }

    private fun <T> KMutableProperty<T>.setRetainOldValue(target: Any, value: T) {
        setter.isAccessible.also { old ->
            setter.isAccessible = true
            setter.call(target, value)
            setter.isAccessible = old
        }
    }
}

internal class TestInitializationException(
        message: String,
        cause: Exception? = null
) : KPotpourriException(message, cause)
