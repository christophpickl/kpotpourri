package com.github.christophpickl.kpotpourri.jackson4k

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.not
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class BuildJackson4kTest {

    companion object {
        private val ANY_STRING = "testString"
    }

    // RENDER

    fun `default config`() {
        assertAsString({ },
                ComplexDto(),
                "{\"string\":\"string\",\"int\":42,\"boolean\":true,\"list\":[\"l1\"]}")
    }

    fun `serializationInclusion - When set to ALWAYS, Then null should be rendered`() {
        assertAsString({ serializationInclusion = JsonInclude.Include.ALWAYS },
                NullableDto(value = null),
                "{\"value\":null}")
    }

    fun `serializationInclusion - When set to NON_NULL, Then null field should be ignored`() {
        assertAsString({ serializationInclusion = JsonInclude.Include.NON_NULL },
                NullableDto(value = null),
                "{}")
    }
    fun `renderNulls - When true, Given null value, Then null field should be rendered`() {
        assertAsString({ renderNulls() },
                NullableDto(value = null),
                "{\"value\":null}")
    }

    fun `renderNulls - When false, Given null value, Then null field should not be rendered`() {
        assertAsString({ renderNoNulls() },
                NullableDto(value = null),
                "{}")
    }

    fun `indent output - When true, Then is intended`() {
        assertAsString({ indentOutput = true },
                StringDto(value = ANY_STRING),
                "{\n  \"value\" : \"$ANY_STRING\"\n}")
    }

    fun `indent output - When false, Then is in one line without whitespace`() {
        assertAsString({ indentOutput = false },
                StringDto(value = ANY_STRING),
                "{\"value\":\"$ANY_STRING\"}")
    }

    fun `orderMapEntries - When true, Then ordered by field names`() {
        assertAsString({ orderMapEntries = true },
                MapDto(linkedMapOf("b" to 2, "a" to 1)),
                "{\"map\":{\"a\":1,\"b\":2}}")
    }

    fun `orderMapEntries - When false, Then order as defined in data class`() {
        assertAsString({ orderMapEntries = false },
                MapDto(linkedMapOf("b" to 2, "a" to 1)),
                "{\"map\":{\"b\":2,\"a\":1}}")
    }

    data class VisibleDto(
            private val privateField: Int = 1,
            val publicField: Int = 2
    )

    fun `visibilities - When set ALL to ANY, Then render all`() {
        val actual = buildJackson4kMapper {
            visibilities += PropertyAccessor.ALL to JsonAutoDetect.Visibility.ANY
        }.asString(VisibleDto())

        assertThat(actual, containsSubstring("privateField"))
    }

    fun `visibilities - When set ALL to PUBLIC, Then render public only`() {
        val actual = buildJackson4kMapper {
            visibilities += PropertyAccessor.ALL to JsonAutoDetect.Visibility.PUBLIC_ONLY
        }.asString(VisibleDto())

        assertThat(actual, not(containsSubstring("privateField")))
    }

    // READ

    fun `failOnUnknownProperties - When enabled, Then throws`() {
        assertThrown<UnrecognizedPropertyException> {
            buildJackson4kMapper { failOnUnknownProperties = true }
                    .readValue<StringDto>("{\"unknown\":\"value\"}")
        }
    }

    fun `failOnUnknownProperties - When false, Then unknown property is ignored`() {
        assertThat(buildJackson4kMapper { failOnUnknownProperties = false }
                .readValue<StringDto>("{\"unknown\":\"value\"}"),
                equalTo(StringDto()))
    }

    private fun assertAsString(withConfig: Jackson4kConfig.() -> Unit, dto: Any, expectedJson: String) {
        assertThat(buildJackson4kMapper(withConfig).asString(dto),
                equalTo(expectedJson.replace("\\\\n", "\\n")))
    }

    data class ComplexDto(
            val string: String = "string",
            val int: Int = 42,
            val boolean: Boolean = true,
            val list: List<String> = listOf("l1")
    )

    data class StringDto(
            val value: String = "string"
    )

    data class NullableDto(
            val value: String? = "string"
    )

    data class MapDto(
            val map: Map<String, Int> = emptyMap()
    )

}
