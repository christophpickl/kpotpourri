package com.github.christophpickl.kpotpourri.common

import org.testng.annotations.Test

@Test class MiscTest {

    fun `enforceAllBranchesCovered sunshine`() {
        val enum = TestableEnum.A
        when (enum) {
            TestableEnum.A -> "A"
            TestableEnum.B -> "B"
        }.enforceAllBranchesCovered
    }

    fun `enforceAllBranchesCovered2 sunshine`() {
        val enum = TestableEnum.A
        @Suppress("UNREACHABLE_CODE") // we still need to do this hack here :-/
        when (enum) {
            TestableEnum.A -> return
            TestableEnum.B -> return
        }.enforceAllBranchesCovered
    }

}

private enum class TestableEnum {
    A, B
}
