package com.github.christophpickl.kpotpourri.jackson4k

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException

/**
 * Changes the rendering/reading behaviour of Jackson in a simple to use and pre-configured way.
 */
data class Jackson4kConfig(

        // READ
        // ====

        /** Throws a [UnrecognizedPropertyException] if the property is unknown. */
        var failOnUnknownProperties: Boolean = false,

        // WRITE
        // =====
        /** Pretty print by adding newlines and indentation spaces. */
        var indentOutput: Boolean = false,

        /** Orders map based on entry keys. */
        var orderMapEntries: Boolean = false,

        /**
         * Should fields with null values rendered or not.
         * (or annotate your DTO: `@JsonIgnoreProperties(ignoreUnknown = true)`)
         */
        var renderNulls: Boolean = true,

        /**
         * Defines which members should be rendered by a minimum defined visibility.
         */
        val visibilities: MutableList<Pair<PropertyAccessor, JsonAutoDetect.Visibility>> = mutableListOf()

)
