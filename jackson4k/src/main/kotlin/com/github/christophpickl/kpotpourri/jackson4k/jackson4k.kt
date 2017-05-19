package com.github.christophpickl.kpotpourri.jackson4k

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.christophpickl.kpotpourri.common.logging.LOG

private val log = LOG {}

/**
 * Simplify access to Jackson configuration.
 */
fun buildJackson4kMapper(withConfig: Jackson4kConfig.() -> Unit = {}): ObjectMapper {
    val config = Jackson4kConfig()
    withConfig.invoke(config)
    log.debug { "buildJackson4kMapper with: $config" }
    return jacksonObjectMapper().apply {
        if (config.indentOutput) {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
        if (config.orderMapEntries) {
            enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
        }
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, config.failOnUnknownProperties)
        if (!config.renderNulls) {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
        // setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        // setSerializationInclusion(JsonInclude.Include.ALWAYS)
    }
}
