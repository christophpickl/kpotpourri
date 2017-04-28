package com.github.christophpickl.kpotpourri.jackson4k

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Informational annotation only.
 *
 * Useful to indicate that one should not simply change the name of properties,
 * as this will lead to changes visible to the client/user, so watch out :)
 */
annotation class JsonObject

/**
 * Simply shortens method name and ensures null safety.
 */
fun ObjectMapper.asString(toBeJsonified: Any): String = writeValueAsString(toBeJsonified)
