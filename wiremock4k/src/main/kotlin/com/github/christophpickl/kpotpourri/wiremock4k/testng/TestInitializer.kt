package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.logging.LOG
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaType


internal object TestInitializer {

    private val log = LOG {}

    internal fun injectPort(testInstance: Any, port: Int) {
        val testClass = testInstance::class
        testClass.memberProperties
//                .filterIsInstance<KMutableProperty<*>>()
                .filter { it.findAnnotation<InjectMockPort>() != null }.forEach { prop ->
            val propType = prop.returnType.javaType
            if (propType != Integer::class.java) {
                throw TestInitializationException("Expected property '${prop.name}' to be of type ${Integer::class.java.name}, but was: ${propType.typeName}")
            }
            if (prop.visibility != KVisibility.PRIVATE) {
                throw TestInitializationException("Expected property '${prop.name}' to be private, but was: ${prop.visibility}")
            }
            if (!prop.isLateinit) {
                throw TestInitializationException("Expected property '${prop.name}' to be marked as lateinit.")
            }
            if (prop !is KMutableProperty<*>) {
                throw TestInitializationException("Expected property '${prop.name}' to be mutable.")
            }
            log.debug { "Setting test property @InjectPort '${testClass.simpleName}.${prop.name}' to: $port" }

            prop.setter.isAccessible.also { old ->
                prop.setter.isAccessible = true
                prop.setter.call(testInstance, port)
                prop.setter.isAccessible = old
            }
        }
    }
}

internal class TestInitializationException(
        message: String,
        cause: Exception? = null
) : KPotpourriException(message, cause)
