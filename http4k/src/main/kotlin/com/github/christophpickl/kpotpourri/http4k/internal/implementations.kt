package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.http4k.internal.implementations.ApacheHttpClientRestClient


internal enum class SupportedImplementation(
        val fqnToLookFor: String,
        val createAdapter: () -> RestClient
) {
    APACHE_HTTP("org.apache.http.impl.client.HttpClients", { ApacheHttpClientRestClient() })
    // FUEL
    // Spring RestTemplate
}

internal object RestClientFactory {

    fun lookupRestClientByImplementation(): RestClient {
        val availableImplementations = SupportedImplementation.values().filter { reflectivelyClassExists(it.fqnToLookFor) }
        return when (availableImplementations.size) {
            0 -> throw KPotpourriException("Could not detect any supported HTTP client! :(")
            1 -> availableImplementations[0].createAdapter()
            else -> throw KPotpourriException("Multiple implementations found: " + availableImplementations.map { it.name }.joinToString(", "))
        }
    }

    private fun reflectivelyClassExists(fqn: String): Boolean {
        try {
            Class.forName(fqn)
            return true
        } catch(e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
