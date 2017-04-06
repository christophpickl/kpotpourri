package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException

enum class HttpMethod {
    GET
}

data class Request(
        val url: String,
        val method: HttpMethod
)

data class Response(
        // header
        // cookies
        val statusCode: Int,
        val bodyAsString: String
)

interface RestClient {
    fun execute(request: Request): Response
}

enum class SupportedImplementation(
        val fqnToLookFor: String,
        val createAdapter: () -> RestClient
) {
    APACHE_HTTP("org.apache.http.impl.client.HttpClients", { ApacheHttpClientRestClient() })
    // FUEL
    // Spring RestTemplate
}

object RestClientFactory {
    fun get(): RestClient {
//        if (reflectivelyClassExists("at.foobar.client")) {
//            return ApacheHttpClientRestClient()
//        }
        val availableImplementations = SupportedImplementation.values().filter { reflectivelyClassExists(it.fqnToLookFor) }
        return when (availableImplementations.size) {
            0 -> throw KPotpourriException("Could not detect any supported HTTP client! :(")
            1 -> availableImplementations[0].createAdapter()
            else -> throw KPotpourriException("Multiple implementations found: " + availableImplementations.map { it.name }.joinToString(", "))
        }
    }

//    private fun instantiate(implementation: SupportedImplementation): RestClient {
//        return Class.forName(implementation.fqnToLookFor).newInstance() as RestClient
//    }

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
