package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.release4k.Version.*
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.Test

@Test class VersionTest {

    private val anyType = VersionType.Release

    fun `increment - version1`() {
        version1(1).increment1() shouldMatchValue version1(2)
    }

    fun `increment - version2`() {
        version2(1, 1).increment1() shouldMatchValue version2(2, 1)
        version2(1, 1).increment2() shouldMatchValue version2(1, 2)
    }

    fun `increment - version3`() {
        version3(1, 1, 1).increment1() shouldMatchValue version3(2, 1, 1)
        version3(1, 1, 1).increment2() shouldMatchValue version3(1, 2, 1)
        version3(1, 1, 1).increment3() shouldMatchValue version3(1, 1, 2)
    }

    fun `increment - version4`() {
        version4(1, 1, 1, 1).increment1() shouldMatchValue version4(2, 1, 1, 1)
        version4(1, 1, 1, 1).increment2() shouldMatchValue version4(1, 2, 1, 1)
        version4(1, 1, 1, 1).increment3() shouldMatchValue version4(1, 1, 2, 1)
        version4(1, 1, 1, 1).increment4() shouldMatchValue version4(1, 1, 1, 2)
    }

    private fun version1(number1: Int) = VersionParts1(anyType, number1)
    private fun version2(number1: Int, number2: Int) = VersionParts2(anyType, number1, number2)
    private fun version3(number1: Int, number2: Int, number3: Int) = VersionParts3(anyType, number1, number2, number3)
    private fun version4(number1: Int, number2: Int, number3: Int, number4: Int) = VersionParts4(anyType, number1, number2, number3, number4)

}
