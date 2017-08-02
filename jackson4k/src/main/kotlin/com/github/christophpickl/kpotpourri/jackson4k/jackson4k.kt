package com.github.christophpickl.kpotpourri.jackson4k

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
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
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)

        config.visibilities.forEach { (accessor, visibility) ->
            setVisibility(accessor, visibility)
        }

        setSerializationInclusion(config.serializationInclusion)
    }
}
