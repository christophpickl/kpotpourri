package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.collection.toPrettyString
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


@Test class ArrayExtensionsTest {

    fun `toPrettyString given empty array should return empty string`() {
        assertThat(arrayOf<String>().toPrettyString(),
                equalTo(""))
    }

    fun `toPrettyString given single element should return single line`() {
        assertThat(arrayOf("a").toPrettyString(),
                equalTo("- a"))
    }

    fun `toPrettyString given two elements should return two lines`() {
        assertThat(arrayOf("a", "b").toPrettyString(),
                equalTo("- a\n- b"))
    }

}
