package com.github.christophpickl.kpotpourri.jackson4k

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
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

    fun `renderNulls - When true, Given null value, Then null field should be rendered`() {
        assertAsString({ renderNulls = true },
                NullableDto(value = null),
                "{\"value\":null}")
    }

    fun `renderNulls - When false, Given null value, Then null field should not be rendered`() {
        assertAsString({ renderNulls = false },
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

    // READ

    fun `failOnUnknownProperties - When enabled, Then throws`() {
        assertThrown<UnrecognizedPropertyException> {
            buildJackson4k { failOnUnknownProperties = true }
                    .readValue<StringDto>("{\"unknown\":\"value\"}")
        }
    }

    fun `failOnUnknownProperties - When false, Then unknown property is ignored`() {
        assertThat(buildJackson4k { failOnUnknownProperties = false }
                .readValue<StringDto>("{\"unknown\":\"value\"}"),
                equalTo(StringDto()))
    }


    private fun assertAsString(withConfig: WithConfig, dto: Any, expectedJson: String) {
        assertThat(buildJackson4k(withConfig).asString(dto),
                equalTo(expectedJson.replace("\\\\n", "\\n")))
    }

    // MINOR TEST arrays, lists, maps

    data class ComplexDto(
            val string: String = "string",
            val int: Int = 42,
            val boolean: Boolean = true,
            //            val array: Array<String> = arrayOf("a1"),
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

//    data class BaDto(
//            val b: String = "b",
//            val a: String = "a"
//    )
}
