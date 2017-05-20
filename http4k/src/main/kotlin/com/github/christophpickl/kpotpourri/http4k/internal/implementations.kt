package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.common.reflection.Reflector
import com.github.christophpickl.kpotpourri.common.reflection.ReflectorImpl
import com.github.christophpickl.kpotpourri.http4k.Http4kException

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

internal class HttpClientFactoryDetector(
        private val reflector: Reflector = ReflectorImpl()
) {

    private val log = LOG {}

    internal fun detect(): HttpClientFactory {
        val availableClients = HttpClientType.values().map { reflector.lookupClass(it.fqnToLookFor) }.filterNotNull()
        if (log.isDebugEnabled) {
            log.debug("Available HTTP4k implementations:")
            availableClients.forEach {
                log.debug("* $it")
            }
        }

        return when (availableClients.size) {
            0 -> throw Http4kException("Http4k could not find any available implementation! Add a new (runtime) dependency for http4k-apache, http4k-fuel, etc.")
            1 -> availableClients[0].newInstance() as HttpClientFactory
            else -> throw Http4kException("Multiple implementations found: " + availableClients.map { it.name }.joinToString(", "))
        }
    }

}
