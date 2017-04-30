package com.github.christophpickl.kpotpourri.common.enum

import com.github.christophpickl.kpotpourri.common.io.readFromStdOut
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class EnumTest {

    fun `toPrettyString - Given TestableEnum, Should format elements in order`() {
        toPrettyString<TestableEnum>() shouldMatchValue "- A\n- B"
    }

    fun `printAllValues as extension - sunshine`() {
        assertThat(readFromStdOut { TestableEnum::class.printAllValues() },
                equalTo("A, B\n"))
    }

    fun `printAllValues - sunshine`() {
        assertThat(readFromStdOut { printAllValues<TestableEnum>() },
                equalTo("A, B\n"))
    }

}

private enum class TestableEnum {
    A, B
}
