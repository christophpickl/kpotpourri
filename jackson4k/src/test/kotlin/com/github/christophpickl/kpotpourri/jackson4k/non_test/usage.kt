package com.github.christophpickl.kpotpourri.jackson4k.non_test

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4kMapper

fun main(args: Array<String>) {

    buildJackson4kMapper {
        // Throws a [UnrecognizedPropertyException] if the property is unknown.
        failOnUnknownProperties = true

        // Pretty print by adding newlines and indentation spaces.
        indentOutput = true

        // Orders map based on entry keys.
        orderMapEntries = true

        // Defines which members should be rendered by a minimum defined visibility.
        visibilities += PropertyAccessor.ALL to JsonAutoDetect.Visibility.ANY

        // Filter which values should be rendered (defaults to always).
        serializationInclusion = JsonInclude.Include.NON_EMPTY
        renderNulls() // => JsonInclude.Include.ALWAYS
        renderNoNulls() // => JsonInclude.Include.NON_NULL
    }

}
