package com.github.christophpickl.kpotpourri.jackson4k

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class Jackson4kTest {

    fun `asString - Should only delegate`() {
        val mapper = ObjectMapper()

        assertThat(mapper.asString(StringDto()),
                equalTo(mapper.writeValueAsString(StringDto())))
    }

    data class StringDto(
            val value: String = "string"
    )
}
