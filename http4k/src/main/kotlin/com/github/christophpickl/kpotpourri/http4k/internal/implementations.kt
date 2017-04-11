package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.collection.toPrettyString
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.Http4kException


internal enum class SupportedImplementation(
        val fqnToLookFor: String
) {
    APACHE_HTTP("com.github.christophpickl.kpotpourri.http4k_apache.ApacheHttpClientRestClient")
    // when adding new implementation
    // 1) add new enum here
    // 2) create new module and add RestClient impl
    // 3) enahnce http4k-tests with new impl tests
    // FUEL
    // Spring RestTemplate
}

internal object RestClientFactory {

    private val log = LOG {}

    fun lookupRestClientByImplementation(): RestClient {
        val availableImplementations = SupportedImplementation.values().map { reflectivelyClassExists(it.fqnToLookFor) }.filterNotNull()
        log.debug { "availableImplementations:\n${availableImplementations.toPrettyString()}" }

        return when (availableImplementations.size) {
            0 -> throw Http4kException("Http4k could not find any available implementation! Add a new (runtime) dependency for http4k-apache, http4k-fuel, etc.")
            1 -> instantiateRestClient(availableImplementations[0])
            else -> throw Http4kException("Multiple implementations found: " + availableImplementations.map { it.name }.joinToString(", "))
        }
    }

    private fun instantiateRestClient(type: Class<*>) =
            type.newInstance() as RestClient

    private fun reflectivelyClassExists(fqn: String): Class<*>? {
        try {
            return Class.forName(fqn)
        } catch(e: ClassNotFoundException) {
            log.trace { "Not found runtime class: $fqn" }
            return null
        }
    }
}
