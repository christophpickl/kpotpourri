package com.github.christophpickl.kotlin_potpourri

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class ListExtensionsTest {

    fun `toPrettyString given empty list should return empty string`() {
        assertThat(emptyList<String>().toPrettyString(),
                equalTo(""))
    }

    fun `toPrettyString given single element should return single line`() {
        assertThat(listOf("a").toPrettyString(),
                equalTo("- a"))
    }

    fun `toPrettyString given two elements should return two lines`() {
        assertThat(listOf("a", "b").toPrettyString(),
                equalTo("- a\n- b"))
    }

}
