package com.github.christophpickl.kpotpourri.george4k

import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get

/**
 * Entry point in order to create a [George4k] instance in a DSL-ish way.
 */
fun buildGeorge4k(): George4k {
    return George4kImpl()
}

/**
 * Core API containing all functionality available.
 */
interface George4k {
    /**
     * Fetch the current version string, e.g. "3.7.0".
     */
    fun version(): String
}

@Suppress("KDocMissingDocumentation")
internal class George4kImpl : George4k {

    private val http4k = buildHttp4k {
        baseUrlBy("https://api.sparkasse.at/proxy/g")
    }

    override fun version(): String {
        val response = http4k.get<VersionJson>("/public/version")
        return response.version
    }

}

internal data class VersionJson(
        /** E.g.: 3.7.0 */
        val version: String,
        /** E.g.: 2017-04-07T13:51:00.000+0200 */
        val buildTime: String,
        /** GIT commit hash. */
        val commit: String,
        /** E.g.: origin/release/3.7.x */
        val branch: String,
        /** E.g.: release-3.7.0 */
        val tags: String

)
