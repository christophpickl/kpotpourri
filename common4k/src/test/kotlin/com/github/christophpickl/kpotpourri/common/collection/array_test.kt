package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.io.readFromStdOut
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test


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

    fun `toPrettyString custom prefix and joiner`() {
        assertThat(arrayOf("a", "b").toPrettyString("* ", "\t"),
                equalTo("* a\t* b"))
    }

    @DataProvider
    fun providePrettyPrintWithoutOptions(): Array<Array<out Any>> = arrayOf(
            arrayOf(emptyArray<String>(), "\n"),
            arrayOf(arrayOf("a"), "- a\n"),
            arrayOf(arrayOf("a", "b"), "- a\n- b\n")
    )

    @Test(dataProvider = "providePrettyPrintWithoutOptions")
    fun `prettyPrint - sunshine`(given: Array<String>, expected: String) {
        assertThat(readFromStdOut { given.prettyPrint() },
                equalTo(expected))
    }

}
