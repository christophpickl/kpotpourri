package com.github.christophpickl.kpotpourri.common.reflection

import mu.KotlinLogging.logger
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

/**
 * In order to create behaviour of java reflection.
 */
interface Reflector {

    /**
     * Try to load the full qualified class at runtime or return `null`.
     */
    fun lookupClass(className: String): Class<*>?

}

/**
 * Default implementation delegating to the "real" behaviour.
 */
class ReflectorImpl : Reflector {

    private val log = logger {}

    override fun lookupClass(className: String): Class<*>? =
        try {
            Class.forName(className)
        } catch(e: ClassNotFoundException) {
            log.debug { "Class not found: $className" }
            null
        }

}

/**
 * Detects all properties of at least type T based on the given instance thiz of type C.
 * 
 * @return suitable member properties in any unpredictable order.
 */
inline fun <reified C : Any, reified T> propertiesOfType(thiz: C): List<T> =
    C::class.memberProperties
        .filter { it.returnType.isSubtypeOf(T::class.createType()) }
        .map { it.get(thiz) as T }
