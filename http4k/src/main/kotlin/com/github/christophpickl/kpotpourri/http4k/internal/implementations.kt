package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.http4k.Http4kException
import com.google.common.annotations.VisibleForTesting

enum class HttpClientType(
        val fqnToLookFor: String
) {
    ApacheHttpClient("com.github.christophpickl.kpotpourri.http4k_apache.ApacheHttpClientFactory"),
    FuelClient("com.github.christophpickl.kpotpourri.http4k_fuel.FuelHttpClientFactory")

    // when adding new implementation
    // 1) add new enum here
    // 2) create new module and add RestClient impl
    // 3) add tests
    // FUEL
    // Spring RestTemplate
}

internal object HttpClientFactoryDetector {

    private val log = LOG {}

    fun detect(): HttpClientFactory {
        val availableClients = HttpClientType.values().map { reflectivelyClassExists(it.fqnToLookFor) }.filterNotNull()
        if (log.isDebugEnabled) {
            log.debug("Available HTTP4k implementations:")
            availableClients.forEach {
                log.debug("* $it")
            }
        }

        return when (availableClients.size) {
            0 -> throw Http4kException("Http4k could not find any available implementation! Add a new (runtime) dependency for http4k-apache, http4k-fuel, etc.")
            1 -> instantiateRestClient(availableClients[0])
            else -> throw Http4kException("Multiple implementations found: " + availableClients.map { it.name }.joinToString(", "))
        }
    }

    @VisibleForTesting fun instantiateRestClient(type: Class<*>) =
            type.newInstance() as HttpClientFactory

    @VisibleForTesting fun reflectivelyClassExists(fqn: String): Class<*>? {
        try {
            return Class.forName(fqn)
        } catch(e: ClassNotFoundException) {
            log.trace { "Not found runtime class: $fqn" }
            return null
        }
    }
}
