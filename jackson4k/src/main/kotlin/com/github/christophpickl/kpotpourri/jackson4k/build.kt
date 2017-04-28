package com.github.christophpickl.kpotpourri.jackson4k

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


/**
 * Changes the rendering/reading behaviour of Jackson in a simple to use and pre-configured way.
 */
data class Jackson4kConfig(
        // RENDER
        // ======
        /** Pretty print by adding newlines and indentation spaces. */
        var indentOutput: Boolean = false,

        /** Orders map based on entry keys. */
        var orderMapEntries: Boolean = false,

        /**
         * Should fields with null values rendered or not.
         * (or annotate your DTO: `@JsonIgnoreProperties(ignoreUnknown = true)`)
         */
        var renderNulls: Boolean = true,

        // READ
        // ====

        /** Throws a [UnrecognizedPropertyException] if the property is unknown. */
        var failOnUnknownProperties: Boolean = false
)

/**
 * Simplify access to Jackson configuration.
 */
fun buildJackson4k(withConfig: WithConfig = {}): ObjectMapper {
    val config = Jackson4kConfig()
    withConfig.invoke(config)

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

/** Change `this` reference in order to operate on (optional) configuration options. */
typealias WithConfig = Jackson4kConfig.() -> Unit
