package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.io.readFromStdOut
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import org.testng.annotations.DataProvider
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

    fun `toPrettyString custom prefix and joiner`() {
        assertThat(listOf("a", "b").toPrettyString("* ", "\t"),
                equalTo("* a\t* b"))
    }

    @DataProvider
    fun providePrettyPrintWithoutOptions(): Array<Array<out Any>> = arrayOf(
            arrayOf(emptyList<String>(), "\n"),
            arrayOf(listOf("a"), "- a\n"),
            arrayOf(listOf("a", "b"), "- a\n- b\n")
    )

    @Test(dataProvider = "providePrettyPrintWithoutOptions")
    fun `prettyPrint - sunshine`(given: List<String>, expected: String) {
        assertThat(readFromStdOut { given.prettyPrint() },
                equalTo(expected))
    }

    fun `plusIf ok`() {
        assertThat(emptyList<String>().plusIf(false, "a"), isEmpty)
        assertThat(emptyList<String>().plusIf(true, "a"), equalTo(listOf("a")))
    }

}
