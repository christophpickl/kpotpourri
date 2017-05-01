package com.github.christophpickl.kpotpourri.common.control

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class ControlTest {


    @DataProvider
    fun provideTakeFirstIfIs(): Array<Array<out Any?>> = arrayOf(
            arrayOf("a", 2, "a"),
            arrayOf(1, "b", "b"),
            arrayOf("a", "b", "a"),
            arrayOf(1, 2, null)
    )

    @Test(dataProvider = "provideTakeFirstIfIs")
    fun `takeFirstIfIs - sunshine`(first: Any, second: Any, expected: String?) {
        takeFirstIfIs<String>(first, second) shouldMatchValue expected
    }

    fun `throwIf - When condition satisfied, Then throws`() {
        val exception = Exception()
        assertThrown<Exception>({ it === exception }) {
            throwIf(true) { exception }
        }
    }

    fun `throwIf - When condition failed, Then not throws`() {
        throwIf(false, ::Exception)
    }

}
