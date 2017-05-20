package com.github.christophpickl.kpotpourri.common.reflection

import com.github.christophpickl.kpotpourri.common.logging.LOG

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

    private val log = LOG {}

    override fun lookupClass(className: String): Class<*>? {
        try {
            return Class.forName(className)
        } catch(e: ClassNotFoundException) {
            log.trace { "Class not found: $className" }
            return null
        }
    }
}
