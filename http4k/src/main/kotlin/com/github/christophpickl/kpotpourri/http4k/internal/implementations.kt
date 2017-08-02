package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.common.reflection.Reflector
import com.github.christophpickl.kpotpourri.common.reflection.ReflectorImpl
import com.github.christophpickl.kpotpourri.http4k.Http4kException

/**
 * By default supported HTTP client implementations.
 */
enum class HttpClientType(
        val fqnToLookFor: String
) {
    /** https://hc.apache.org/httpcomponents-client-ga/ */
    ApacheHttpClient("com.github.christophpickl.kpotpourri.http4k_apache.ApacheHttpClientFactory"),

    /** https://github.com/kittinunf/Fuel */
    FuelClient("com.github.christophpickl.kpotpourri.http4k_fuel.FuelHttpClientFactory")

    // Spring RestTemplate

    // when adding new implementation
    // 1) add new enum here
    // 2) create new module and add RestClient impl
    // 3) add tests
}

internal class HttpClientFactoryDetector(
        private val reflector: Reflector = ReflectorImpl()
) {

    private val log = LOG {}

    internal fun detect(): HttpClientFactory {
        val availableClients = HttpClientType.values().map { reflector.lookupClass(it.fqnToLookFor) }.filterNotNull()

        log.debug("Available HTTP4k implementations:")
        availableClients.forEach {
            log.debug { "* $it" }
        }

        return when (availableClients.size) {
            1 -> availableClients[0].newInstance() as HttpClientFactory
            0 -> throw Http4kException("Http4k could not find any available implementation! Add a new (runtime) dependency for http4k-apache, http4k-fuel, etc.")
            else -> throw Http4kException("Multiple implementations found: " + availableClients.map { it.name }.joinToString(", "))
        }
    }

}
