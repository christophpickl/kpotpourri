package com.github.christophpickl.kpotpourri.common.enum

import com.github.christophpickl.kpotpourri.common.io.Io
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.Test

@Test class EnumTest {

    fun `toPrettyString - Given TestableEnum, Should format elements in order`() {
        toPrettyString<TestableEnum>() shouldMatchValue "- A\n- B"
    }

    fun `toPrettyString - Given TestableEnum with custom prefix and joiner, Should format elements properly`() {
        toPrettyString<TestableEnum>(prefix = "x", joiner = "y") shouldMatchValue "xAyxB"
    }

    fun `printAllValues - Given TestableEnum, Should listify output`() {
        Io.readFromStdOut { printAllValues<TestableEnum>() } shouldMatchValue "A, B\n"
    }

    fun `printAllValues as extension - Given TestableEnum, Should listify output`() {
        Io.readFromStdOut { TestableEnum::class.printAllValues() } shouldMatchValue "A, B\n"
    }

}

private enum class TestableEnum {
    A, B
}
