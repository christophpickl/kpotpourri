package com.github.christophpickl.kpotpourri.jackson4k

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Informational annotation only.
 *
 * Useful to indicate that one should not simply change the name of properties,
 * as this will lead to changes visible to the client/user, so watch out :)
 */
annotation class JsonObject

/**
 * Simplify access to Jackson configuration.
 */
fun buildJackson4kObjectMapper(
        // or use: @JsonIgnoreProperties(ignoreUnknown = true) for your DTO
        indentOutput: Boolean = true,
        orderMapEntries: Boolean = true,
        failOnUnknownProperties: Boolean = false,
        renderNulls: Boolean = false
): ObjectMapper {
    return jacksonObjectMapper().apply {
        if (indentOutput) {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
        if (orderMapEntries) {
            enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
        }
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties)
        // renderNulls ... // FIXME implement me
        // setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        // setSerializationInclusion(JsonInclude.Include.ALWAYS)
    }
}

fun ObjectMapper.asString(toBeJsonified: Any) = writeValueAsString(toBeJsonified)!!
